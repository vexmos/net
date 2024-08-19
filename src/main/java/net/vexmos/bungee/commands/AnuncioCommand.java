package net.vexmos.bungee.commands;

import net.vexmos.bungee.Bungee;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Arrays;
import java.util.List;

public class AnuncioCommand extends Commands {

    public AnuncioCommand() {
        super("anuncio", "anunciar", "broadcast", "announce", "announcement");
    }


    private static final List<String> REQUIRED_PERMISSIONS = Arrays.asList(
            "group.diretor", "group.dev", "group.admin"
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
    public void perform(CommandSender sender, String[] args) {
        if (!hasRequiredPermission(sender)) {
            sender.sendMessage(new TextComponent("§cVocê não tem permissão para isso."));
            return;
        }

        if (args.length < 1) {
            sender.sendMessage(new TextComponent("§cUso: /anuncio <mensagem>"));
            return;
        }

        String message = String.join(" ", args);

        if (args[0].equalsIgnoreCase("local")) {
            if (sender instanceof ProxiedPlayer) {
                ProxiedPlayer player = (ProxiedPlayer) sender;

                for (ProxiedPlayer server : player.getServer().getInfo().getPlayers()){
                    server.sendMessage(new TextComponent("§a[Anúncio] " + message.replaceFirst("local", "").replaceAll("&", "§")));
                }
            } else {
                sender.sendMessage(new TextComponent("§cEste comando só pode ser executado por jogadores."));
            }
        } else {
            Bungee.get().getProxy().broadcast(new TextComponent("§6§lVEXMOS §8→ §f" + message.replaceAll("&", "§")));
        }
    }
}
