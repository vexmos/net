package net.vexmos.spigot.commands;

import net.vexmos.spigot.listeners.player.messages.ChatListener;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatCommand extends Commands {

    public ChatCommand() {
        super("chat", "chatear");
        this.setPermission("system.cmd.chat");
    }

    @Override
    public void perform(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cApenas Players podem usar este comando.");
            return;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            if (ChatListener.isChatEnabled()) {
                player.sendMessage("§cO chat está desabilitado desde: §f" + ChatListener.getLastDisabledTime());
            } else {
                player.sendMessage("§aO chat está habilitado.");
            }
            return;
        }



        boolean newState = !ChatListener.isChatEnabled();
        ChatListener.setChatState(newState, null);

        if (newState) {
            player.sendMessage("§aO chat foi habilitado.");
        } else {
            player.sendMessage("§cO chat foi desabilitado.");
        }
    }


}
