package net.vexmos.spigot.commands;

import net.vexmos.spigot.api.SpigotConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class PosCommand extends Commands {

    private final SpigotConfig config;

    public PosCommand() {
        super("pos", "position");
        this.config = new SpigotConfig("positions.yml");
        config.saveDefaultConfig();
    }

    private static final List<String> REQUIRED_PERMISSIONS = Arrays.asList(
            "group.diretor", "group.dev"
    );

    private boolean hasRequiredPermission(CommandSender sender) {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (sender.hasPermission(permission)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void perform(CommandSender sender, String label, String[] args) {
        if(!hasRequiredPermission(sender)) {
            sender.sendMessage("§cVocê não tem permissão para isto.");
            return;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cApenas Players podem usar este comando.");
            return;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage("§cUso: /pos <set|remove|list|tp> <nomeposicao>");
            return;
        }

        String subcommand = args[0];

        switch (subcommand.toLowerCase()) {
            case "set":
                if (args.length < 2) {
                    player.sendMessage("§cUso: /pos set <nomeposicao>");
                    return;
                }
                setPosition(player, args[1]);
                break;

            case "remove":
                if (args.length < 2) {
                    player.sendMessage("§cUso: /pos remove <nomeposicao>");
                    return;
                }
                removePosition(player, args[1]);
                break;

            case "list":
                listPositions(player);
                break;

            case "tp":
                if (args.length < 2) {
                    player.sendMessage("§cUso: /pos tp <nomeposicao>");
                    return;
                }
                teleportToPosition(player, args[1]);
                break;

            default:
                player.sendMessage("§cUso: /pos <set|remove|list|tp> <nomeposicao>");
                break;
        }
        return;
    }

    private void setPosition(Player player, String name) {
        Location loc = player.getLocation();
        config.getConfig().set(name + ".world", loc.getWorld().getName());
        config.getConfig().set(name + ".x", loc.getX());
        config.getConfig().set(name + ".y", loc.getY());
        config.getConfig().set(name + ".z", loc.getZ());
        config.getConfig().set(name + ".yaw", loc.getYaw());
        config.getConfig().set(name + ".pitch", loc.getPitch());
        config.saveConfig();
        player.sendMessage("§aPosição " + name + " salva com sucesso.");
    }

    private void removePosition(Player player, String name) {
        if (config.getConfig().contains(name)) {
            config.getConfig().set(name, null);
            config.saveConfig();
            player.sendMessage("§aPosição " + name + " removida com sucesso.");
        } else {
            player.sendMessage("§cPosição " + name + " não encontrada.");
        }
    }

    private void listPositions(Player player) {
        Set<String> positions = config.getConfig().getKeys(false);
        if (positions.isEmpty()) {
            player.sendMessage("§cNenhuma posição encontrada.");
        } else {
            player.sendMessage("§aPosições salvas:");
            for (String position : positions) {
                player.sendMessage("§e- " + position);
            }
        }
    }

    public static void teleportToPosition(Player player, String name) {
        SpigotConfig config = new SpigotConfig("positions.yml");
        if (config.getConfig().contains(name)) {
            String worldName = config.getConfig().getString(name + ".world");
            double x = config.getConfig().getDouble(name + ".x");
            double y = config.getConfig().getDouble(name + ".y");
            double z = config.getConfig().getDouble(name + ".z");
            float yaw = (float) config.getConfig().getDouble(name + ".yaw");
            float pitch = (float) config.getConfig().getDouble(name + ".pitch");

            Location loc = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
            player.teleport(loc);
            player.sendMessage("§aTeleportado para a posição " + name + ".");
        } else {
            player.sendMessage("§cErro de posição mapeada.");
        }
    }
}
