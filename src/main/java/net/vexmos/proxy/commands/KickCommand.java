package net.vexmos.proxy.commands;

import net.vexmos.proxy.VexmosPROXY;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Arrays;
import java.util.List;

public class    KickCommand extends Commands {

    public KickCommand() {
        super("kick", "expulsar");
    }

    private static final List<String> REQUIRED_PERMISSIONS = Arrays.asList(
            "group.diretor", "group.dev", "group.mod", "group.admin"
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
            sender.sendMessage(new TextComponent("§cUso: /kick <jogador>"));
            return;
        }

        String playerName = args[0];
        ProxiedPlayer player = VexmosPROXY.get().getProxy().getPlayer(playerName);

        if (player == null) {
            sender.sendMessage(new TextComponent("§cEste jogador não está online no servidor."));
            return;
        }

        player.disconnect(new TextComponent("§cSua conexão ao servidor foi interrompida."));
        sender.sendMessage(new TextComponent("§aJogador " + playerName + " foi kickado do servidor."));
    }
}
