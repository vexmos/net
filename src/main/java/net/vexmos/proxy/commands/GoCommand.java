package net.vexmos.proxy.commands;

import net.vexmos.proxy.VexmosPROXY;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Arrays;
import java.util.List;

public class GoCommand extends Commands {

    public GoCommand() {
        super("go", "ir", "goto");
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
            sender.sendMessage(new TextComponent("§cVocê não tem permissão para executar este comando."));
            return;
        }

        if (args.length < 1) {
            sender.sendMessage(new TextComponent("§cUso: /go <jogador>"));
            return;
        }

        String playerName = args[0];
        ProxiedPlayer player = (ProxiedPlayer) sender;
        ProxiedPlayer targetPlayer = VexmosPROXY.get().getProxy().getPlayer(playerName);

        if (targetPlayer == null) {
            sender.sendMessage(new TextComponent("§cEste jogador não está online no servidor."));
            return;
        }

        if (sender.getName().equals(targetPlayer.getName())){
            player.sendMessage(new TextComponent("§cVocê não pode ir até a si mesmo"));
            return;
        }

        if (targetPlayer.getServer().getInfo().getName() == player.getServer().getInfo().getName()) {
            sender.sendMessage(new TextComponent("§cVocê já está no servidor do jogador §e" + targetPlayer));
            return;
        }

        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new TextComponent("§cEste comando só pode ser usado por jogadores."));
            return;
        }



        player.connect(targetPlayer.getServer().getInfo());

        player.sendMessage(new TextComponent("§aVocê foi enviado para " + targetPlayer.getName() + "."));

    }
}