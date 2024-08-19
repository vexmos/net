package net.vexmos.spigot.api;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("ALL")
public class ImageHandlerAPI {

    public static List<Location> imageLocations = new ArrayList<>();
    static SpigotConfig config = new SpigotConfig("imagedata.yml");
    private static final File imagesFolder = new File(Bukkit.getPluginManager().getPlugin("VexmosNET").getDataFolder(), "images");
    public static Player player;

    public static void setup() {
        if (!imagesFolder.exists()) imagesFolder.mkdirs();
        config.saveConfig();
        loadImages();
    }

    public static File getImagesFolder() {
        return imagesFolder;
    }

    public static void applyImageToFrames(Player player, String imageName) {
        File imageFile = new File(imagesFolder, imageName);
        if (!imageFile.exists()) {
            player.sendMessage("Imagem não encontrada.");
            return;
        }

        CompletableFuture.supplyAsync(() -> {
            try {
                BufferedImage image = ImageIO.read(imageFile);
                if (image == null) throw new IOException("Imagem não carregada.");

                BufferedImage resizedImage = resizeImage(image, 384, 192);
                return new ImageData(resizedImage, findNearbyItemFrames(player, 28));
            } catch (IOException e) {
                Bukkit.getLogger().severe("Erro ao carregar a imagem: " + e.getMessage());
                return null;
            }
        }).thenAccept(imageData -> {
            if (imageData == null) {
                player.sendMessage("Erro ao processar a imagem.");
                return;
            }

            List<ItemFrame> frames = imageData.frames;
            BufferedImage resizedImage = imageData.image;

            if (frames.size() < 28) {
                player.sendMessage("Não há frames suficientes para mostrar a imagem.");
                return;
            }

            frames.sort((f1, f2) -> {
                Location loc1 = f1.getLocation();
                Location loc2 = f2.getLocation();
                int result = Double.compare(loc1.getY(), loc2.getY());
                if (result == 0) {
                    result = Double.compare(loc1.getZ(), loc2.getZ());
                    if (result == 0) result = Double.compare(loc1.getX(), loc2.getX());
                }
                return result;
            });

            int rows = 4;
            int cols = 7;
            int frameWidth = resizedImage.getWidth() / cols;
            int frameHeight = resizedImage.getHeight() / rows;

            Vector direction = player.getEyeLocation().getDirection();
            String orientation = getOrientationFromDirection(direction);

            int[] columnMapping;
            int[] rowMapping;

            switch (orientation) {
                case "west":
                case "south":
                    columnMapping = new int[]{6, 5, 4, 3, 2, 1, 0};
                    rowMapping = new int[]{3, 2, 1, 0};
                    break;
                case "east":
                case "north":
                    columnMapping = new int[]{0, 1, 2, 3, 4, 5, 6};
                    rowMapping = new int[]{3, 2, 1, 0};
                    break;
                default:
                    player.sendMessage("Orientação inválida.");
                    return;
            }

            for (int row = 0; row < rows; row++) {
                int mappedRow = rowMapping[row];
                for (int col = 0; col < cols; col++) {
                    int mappedCol = columnMapping[col];
                    int x = mappedCol * frameWidth;
                    int y = mappedRow * frameHeight;
                    BufferedImage subImage = resizedImage.getSubimage(x, y, frameWidth, frameHeight);
                    MapView mapView = Bukkit.createMap(player.getWorld());
                    mapView.addRenderer(new ImageMapRenderer(subImage));
                    int frameIndex = row * cols + col;
                    if (frameIndex < frames.size()) {
                        ItemFrame frame = frames.get(frameIndex);
                        frame.setItem(new ItemStack(Material.MAP, 1, mapView.getId()));
                    } else {
                        player.sendMessage("Erro ao aplicar a imagem aos frames.");
                        return;
                    }
                }
            }

            saveImageData(imageName, frames);
        });
    }

    private static String getOrientationFromDirection(Vector direction) {
        double x = direction.getX();
        double z = direction.getZ();
        if (Math.abs(x) > Math.abs(z)) {
            return x > 0 ? "east" : "west";
        } else {
            return z > 0 ? "south" : "north";
        }
    }

    public static void removeImageFromFrame(ItemFrame frame) {
        List<Location> locations = getImageLocationsFromConfig();
        for (Location location : locations) {
            Block block = location.getBlock();
            if (block.getState() instanceof ItemFrame) {
                ItemFrame itemFrame = (ItemFrame) block.getState();
                if (itemFrame.equals(frame)) {
                    String imageName = getImageNameFromLocation(location);
                    File imageFile = new File(imagesFolder, imageName);
                    if (imageFile.exists()) imageFile.delete();
                    resetImages(null);
                    break;
                }
            }
        }
    }

    private static List<Location> getImageLocationsFromConfig() {
        return new ArrayList<>();
    }

    private static String getImageNameFromLocation(Location location) {
        return "";
    }

    public static void resetImages(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Location location : imageLocations) {
                    Block block = location.getBlock();
                    if (block.getState() instanceof ItemFrame) {
                        ItemFrame frame = (ItemFrame) block.getState();
                        frame.setItem(null);
                    }
                }
                imageLocations.clear();
                config.getConfig().set("imageData", null);
                config.saveConfig();
            }
        }.runTask(Bukkit.getPluginManager().getPlugin("LunikSystem"));
    }

    public static void removeImage(Player player, String imageName) {
        File imageFile = new File(imagesFolder, imageName);
        if (imageFile.exists()) {
            imageFile.delete();
            player.sendMessage("Imagem removida com sucesso.");
        } else {
            player.sendMessage("Imagem não encontrada.");
        }
        resetImages(player);
    }

    public static List<ItemFrame> findNearbyItemFrames(Player player, int maxFrames) {
        List<ItemFrame> frames = new ArrayList<>();
        for (Entity entity : player.getNearbyEntities(5.0D, 5.0D, 5.0D)) {
            if (entity instanceof ItemFrame) {
                frames.add((ItemFrame) entity);
                if (frames.size() >= maxFrames) break;
            }
        }
        return frames;
    }

    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        g2d.dispose();
        return resizedImage;
    }

    public static void loadImages() {
        if (config.getConfig().contains("imageData")) {
            for (String imageName : config.getConfig().getConfigurationSection("imageData").getKeys(false)) {
                List<Location> locations = (List<Location>) config.getConfig().get("imageData." + imageName + ".locations");
                String type = config.getConfig().getString("imageData." + imageName + ".type");
                if (locations != null && type != null && type.equals("gif")) {
                    File imageFile = new File(imagesFolder, imageName);
                    if (imageFile.exists()) {
                        List<ItemFrame> frames = new ArrayList<>();
                        for (Location loc : locations) {
                            Block block = loc.getBlock();
                            if (block.getState() instanceof ItemFrame) frames.add((ItemFrame) block.getState());
                        }
                        applyImageToFrames(player, imageName);
                    }
                }
            }
        }
    }

    public static void saveImageData(String imageName, List<ItemFrame> frames) {
        List<Location> locations = new ArrayList<>();
        for (ItemFrame frame : frames) locations.add(frame.getLocation());
        config.getConfig().set("imageData." + imageName + ".locations", locations);
        config.getConfig().set("imageData." + imageName + ".type", "gif");
        config.saveConfig();
    }

    private static class ImageMapRenderer extends MapRenderer {
        private final BufferedImage image;

        public ImageMapRenderer(BufferedImage image) {
            this.image = image;
        }

        @Override
        public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
            mapCanvas.drawImage(0, 0, MapPalette.resizeImage(this.image));
        }
    }

    private static class ImageData {
        BufferedImage image;
        List<ItemFrame> frames;

        public ImageData(BufferedImage image, List<ItemFrame> frames) {
            this.image = image;
            this.frames = frames;
        }
    }
}
