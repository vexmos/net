package net.vexmos.spigot.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class TpCommand extends Commands {

    private static final List<String> REQUIRED_PERMISSIONS = Arrays.asList(
            "group.diretor", "group.dev", "group.mod", "group.construtor"
    );

    public TpCommand() {
        super("tp", "teleport", "teleportar");
    }

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
        if (!hasRequiredPermission(sender)) {
            sender.sendMessage("§cVocê não tem permissão para isto.");
            return;
        }

        if (args.length == 0) {
            sender.sendMessage("§cUso: /tp <player> ou /tp <player1> <player2> ou /tp <player1> <x> <y> <z> ou /tp <x> <y> <z>");
            return;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("§cApenas Players podem usar este comando.");
            return;
        }

        Player player = (Player) sender;

        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null) {
                player.teleport(target.getLocation());
                player.sendMessage("§aTeleportado para " + target.getName());
            } else {
                player.sendMessage("§cJogador não encontrado.");
            }
        } else if (args.length == 2) {
            Player target1 = Bukkit.getPlayer(args[0]);
            Player target2 = Bukkit.getPlayer(args[1]);
            if (target1 != null && target2 != null) {
                target1.teleport(target2.getLocation());
                target1.sendMessage("§aTeleportado para " + target2.getName());
            } else {
                player.sendMessage("§cJogador não encontrado.");
            }
        } else if (args.length == 3) {
            try {
                double x = Double.parseDouble(args[0]);
                double y = Double.parseDouble(args[1]);
                double z = Double.parseDouble(args[2]);
                Location loc = new Location(player.getWorld(), x, y, z);
                player.teleport(loc);
                player.sendMessage("§aTeleportado para (" + x + ", " + y + ", " + z + ")");
            } catch (NumberFormatException e) {
                player.sendMessage("§cCoordenadas inválidas.");
            }
        } else if (args.length == 4) {
            try {
                double x = Double.parseDouble(args[1]);
                double y = Double.parseDouble(args[2]);
                double z = Double.parseDouble(args[3]);
                Location loc = new Location(Bukkit.getWorld(args[0]), x, y, z);
                player.teleport(loc);
                player.sendMessage("§aTeleportado para (" + x + ", " + y + ", " + z + ") em " + args[0]);
            } catch (NumberFormatException e) {
                player.sendMessage("§cCoordenadas inválidas.");
            }
        } else {
            player.sendMessage("§cUso: /tp <player> ou /tp <player1> <player2> ou /tp <player1> <x> <y> <z> ou /tp <x> <y> <z>");
        }
    }
}