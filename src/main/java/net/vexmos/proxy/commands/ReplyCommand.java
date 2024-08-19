package net.vexmos.proxy.commands;

import net.vexmos.proxy.VexmosPROXY;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;
import java.util.Map;

public class ReplyCommand extends Commands {
    private static final Map<String, String> lastMessage = new HashMap<>();

    public ReplyCommand() {
        super("r", "responder");
    }

    public static void setLastMessage(ProxiedPlayer sender, ProxiedPlayer receiver) {
        lastMessage.put(sender.getName(), receiver.getName());
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new TextComponent("§cApenas jogadores podem usar este comando."));
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;
        if (player.getServer().getInfo().getName().equalsIgnoreCase("login")) {
            player.sendMessage(new TextComponent("§cVocê não pode usar este comando no servidor de login."));
            return;
        }

        if (args.length < 1) {
            sender.sendMessage(new TextComponent("§cUso /r <mensagem>"));
            return;
        }

        String lastSenderName = lastMessage.get(sender.getName());
        if (lastSenderName == null) {
            sender.sendMessage(new TextComponent("§cVocê não anda conversando ultimamente."));
            return;
        }

        ProxiedPlayer lastSender = VexmosPROXY.get().getProxy().getPlayer(lastSenderName);
        if (lastSender == null || BlockCommand.isBlocked(lastSender, player)) {
            sender.sendMessage(new TextComponent("§cAlvo não encontrado."));
            return;
        }

        StringBuilder messageBuilder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            messageBuilder.append(args[i]).append(" ");
        }
        String message = messageBuilder.toString().trim();

        String formattedMessage = String.format("§8[§7%s §f» §7%s§8] §e%s", player.getName(), lastSender.getName(), message);

        lastSender.sendMessage(new TextComponent(formattedMessage));
        sender.sendMessage(new TextComponent(formattedMessage));
    }
}
