package net.vexmos.proxy.commands;

import net.vexmos.proxy.VexmosPROXY;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Arrays;
import java.util.List;

public class ClearChatCommand extends Commands {

    public ClearChatCommand() {
        super("cc", "clearchat", "limparchat");
    }


    private static final List<String> REQUIRED_PERMISSIONS = Arrays.asList(
            "group.diretor", "group.dev", "group.mod", "group.admin", "group.suporte"
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

        if (args.length == 0) {
            // Clear chat of the player's executor server chat
            if (sender instanceof ProxiedPlayer) {
                ProxiedPlayer player = (ProxiedPlayer) sender;
                for (int i = 0; i < 100; i++) {
                    player.sendMessage(new TextComponent(" "));
                }
                sender.sendMessage(new TextComponent("§aChat limpo com sucesso!"));
            } else {
                sender.sendMessage(new TextComponent("§cEste comando só pode ser executado por jogadores."));
            }
        } else if (args.length == 1) {
            String arg = args[0].toLowerCase();
            if (arg.equals("help") || arg.equals("ajuda")) {
                sender.sendMessage(new TextComponent("§cUso: /clearchat [geral | <server> | help]"));
                return;
            }

            if (arg.equals("all") || arg.equals("geral") || arg.equals("general")) {
                // Clear chat of all servers
                VexmosPROXY.get().getProxy().getPlayers().forEach((player) -> {
                    for (int i = 0; i < 100; i++) {
                        player.sendMessage(new TextComponent(" "));
                    }
                });
                sender.sendMessage(new TextComponent("§aChat limpo em todos os servidores!"));
            } else {
                // Clear chat of a specific server
                String serverName = arg;
                ServerInfo server = ProxyServer.getInstance().getServerInfo(serverName);
                if (server != null) {
                    server.getPlayers().forEach((player) -> {
                        for (int i = 0; i < 100; i++) {
                            player.sendMessage(new TextComponent(" "));
                        }
                    });
                    sender.sendMessage(new TextComponent("§aChat limpo no servidor " + arg + "!"));
                } else {
                    sender.sendMessage(new TextComponent("§cServidor não encontrado."));
                }
            }
        } else {
            sender.sendMessage(new TextComponent("§cUso: /clearchat [all/geral/general|<server>]"));
        }
    }
}
