package net.vexmos.spigot.commands;

import net.vexmos.spigot.api.holograms.HologramApi;
import net.vexmos.spigot.api.holograms.IHologram;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HologramaCommand extends Commands {

    private static final Map<String, IHologram> holograms = new HashMap<>();

    public HologramaCommand() {
        super("holograma", "hologram");
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
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cApenas Players podem usar este comando.");
            return;
        }

        if(!hasRequiredPermission(sender)) {
            sender.sendMessage("§cVocê não tem permissão para isto.");
            return;
        }

        assert sender instanceof Player;
        Player player = (Player) sender;

        if (args.length == 0) {
            sender.sendMessage("§cUso: /holograma <criar/deletar/modificar> <nome> <texto>");
            return;
        }

        String subCommand = args[0];
        String hologramName = args[1];

        switch (subCommand) {
            case "criar":
            case "create":
                if (args.length < 3) {
                    sender.sendMessage("§cUso: /holograma criar <nome> <texto>");
                    return;
                }

                String text = args[2];
                for (int i = 3; i < args.length; i++) {
                    text += " " + args[i];
                }

                text = text.replace("&", "§");

                Location location = player.getLocation();
                IHologram hologram = HologramApi.createNewHologram(new String[]{text}, location);
                holograms.put(hologramName, hologram);
                sender.sendMessage("§aHolograma criado com sucesso!");
                break;

            case "deletar":
            case "delete":
                if (holograms.containsKey(hologramName)) {
                    hologram = holograms.get(hologramName);
                    hologram.hide();
                    holograms.remove(hologramName);
                    sender.sendMessage("§aHolograma deletado com sucesso!");
                } else {
                    sender.sendMessage("§cHolograma não encontrado.");
                }
                break;

            case "modificar":
            case "modify":
                if (args.length < 3) {
                    sender.sendMessage("§cUso: /holograma modificar <nome> texto <novo_texto>");
                    return;
                }

                if (holograms.containsKey(hologramName)) {
                    hologram = holograms.get(hologramName);
                    String newText = args[3];
                    for (int i = 4; i < args.length; i++) {
                        newText += " " + args[i];
                    }

                    newText = newText.replace("&", "§");

                    hologram.hide();
                    hologram = HologramApi.createNewHologram(new String[]{newText}, player.getLocation());
                    holograms.put(hologramName, hologram);
                    sender.sendMessage("§aHolograma modificado com sucesso!");
                } else {
                    sender.sendMessage("§cHolograma não encontrado.");
                }
                break;

            default:
                sender.sendMessage("§cUso: /holograma <criar/deletar/modificar> <nome> <texto>");
                break;
        }
    }
}