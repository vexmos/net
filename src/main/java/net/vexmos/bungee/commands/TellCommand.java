package net.vexmos.bungee.commands;

import net.vexmos.bungee.Bungee;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.vexmos.database.bungee.ConnectBungee;

public class TellCommand extends Commands {

    public TellCommand() {
        super("tell", "private", "privado");
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        ConnectBungee db = new ConnectBungee();
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new TextComponent("§cApenas jogadores podem usar este comando."));
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;
        if (player.getServer().getInfo().getName().equalsIgnoreCase("login")) {
            player.sendMessage(new TextComponent("§cVocê deve se autenticar para fazer isto."));
            return;
        }

        if(!(db.isTellEnabled(sender.getName()))) {
            sender.sendMessage("§cVocê está com as mensagens privadas desativadas.");
            return;
        }

        if (args.length < 2) {
            sender.sendMessage(new TextComponent("§cUso /tell <destino> <mensagem>"));
            return;
        }

        if (player.getName().equalsIgnoreCase(args[0])) {
            sender.sendMessage(new TextComponent("§cVocê não pode enviar uma mensagem para si mesmo."));
            return;
        }



        ProxiedPlayer target = Bungee.get().getProxy().getPlayer(args[0]);
        if (target == null || BlockCommand.isBlocked(target, player)) {
            sender.sendMessage(new TextComponent("§cAlvo não encontrado."));
            return;
        }

        if(!(db.isTellEnabled(target.getName()))) {
            sender.sendMessage("§cEsse jogador não está recebendo mensagens privadas.");
            return;
        }

        StringBuilder messageBuilder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            messageBuilder.append(args[i]).append(" ");
        }
        String message = messageBuilder.toString().trim();

        String formattedMessage = String.format("§8[§7%s §f» §7%s§8] §e%s", player.getName(), target.getName(), message);

        target.sendMessage(new TextComponent(formattedMessage));
        sender.sendMessage(new TextComponent(formattedMessage));
    }
}
