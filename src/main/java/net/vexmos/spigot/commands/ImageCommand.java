//package net.vexmos.spigot.commands;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.Arrays;
//import java.util.List;
//
//import net.vexmos.spigot.api.ImageHandlerAPI;
//import org.bukkit.command.CommandSender;
//import org.bukkit.entity.Player;
//
//public class ImageCommand extends Commands {
//
//
//
//
//    protected ImageCommand() {
//        super("image", new String[] { "imagem" });
//    }
//
//    private static final List<String> REQUIRED_PERMISSIONS = Arrays.asList(
//            "group.diretor", "group.dev"
//    );
//
//    private boolean hasRequiredPermission(CommandSender sender) {
//        for (String permission : REQUIRED_PERMISSIONS) {
//            if (sender.hasPermission(permission)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public void perform(CommandSender sender, String label, String[] args) {
//        String input, removeName;
//        if (!(sender instanceof Player)) {
//            sender.sendMessage("§cComando para jogadores.");
//            return;
//        }
//
//        assert sender instanceof Player;
//        Player player = (Player) sender;
//
//
//        if (!hasRequiredPermission(player)) {
//            sender.sendMessage("§cVocê não tem permissão para isto.");
//            return;
//        }
//        if (args.length < 1) {
//            sender.sendMessage("/img <load/reset/remove> [url/nome da imagem]");
//            return;
//        }
//        String command = args[0].toLowerCase();
//        switch (command) {
//            case "load":
//                if (args.length != 2) {
//                    sender.sendMessage("/img load <url/nome da imagem>");
//                    return;
//                }
//                input = args[1];
//                if (isValidURL(input)) {
//                    try {
//                        String imageName = downloadImage(input);
//                        if (imageName != null) {
//                            ImageHandlerAPI.applyImageToFrames(player, imageName);
//                            sender.sendMessage("carregada com sucesso.");
//                        } else {
//                            sender.sendMessage("ao baixar a imagem.");
//                        }
//                    } catch (IOException e) {
//                        sender.sendMessage("ao baixar a imagem: " + e.getMessage());
//                        e.printStackTrace();
//                    }
//                } else {
//                    ImageHandlerAPI.applyImageToFrames(player, input);
//                }
//                return;
//            case "reset":
//                ImageHandlerAPI.resetImages(player);
//                sender.sendMessage("as imagens foram removidas.");
//                return;
//            case "remove":
//                if (args.length != 2) {
//                    sender.sendMessage("/img remove <nome da imagem>");
//                    return;
//                }
//                removeName = args[1];
//                ImageHandlerAPI.removeImage(player, removeName);
//                return;
//        }
//        sender.sendMessage("desconhecido.");
//    }
//
//    private boolean isValidURL(String url) {
//        try {
//            new URL(url);
//            return (url.endsWith(".png") || url.endsWith(".jpeg") || url.endsWith(".jpg"));
//        } catch (MalformedURLException e) {
//            return false;
//        }
//    }
//
//    private String downloadImage(String imageUrl) throws IOException {
//        URL url = new URL(imageUrl);
//        String fileName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
//        File imageFile = new File(ImageHandlerAPI.getImagesFolder(), fileName);
//        try(InputStream in = url.openStream();
//            OutputStream out = new FileOutputStream(imageFile)) {
//            byte[] buffer = new byte[1024];
//            int bytesRead;
//            while ((bytesRead = in.read(buffer)) != -1)
//                out.write(buffer, 0, bytesRead);
//        }
//        return imageFile.exists() ? fileName : null;
//    }
//}
