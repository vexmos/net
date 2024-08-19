package net.vexmos.proxy.commands;

import net.vexmos.proxy.VexmosPROXY;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PingCommand extends Commands {

    public PingCommand() {
        super("ping", "ms");
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new TextComponent("§cApenas jogadores podem usar este comando."));
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;

        // Envia a primeira mensagem
        player.sendMessage(new TextComponent("§aChecando suas informações de rede..."));

        // Espera 1 segundo antes de enviar a próxima mensagem
        VexmosPROXY.get().getProxy().getScheduler().schedule(VexmosPROXY.get(), () -> {
            // Envia a segunda mensagem
            player.sendMessage(new TextComponent("§aSuas informações foram verificadas com sucesso"));

            // Pula uma linha pra ficar bonitin
            player.sendMessage(new TextComponent(""));

            // ms ping
            String pingMessage = "§e" + player.getPing() + "ms";
            TextComponent pingComponent = new TextComponent(pingMessage);

            // hover effect
            String ip = player.getAddress().getAddress().getHostAddress();
            pingComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§eSeu IP\n§8- §a" + ip).create()));

            // message ping
            player.sendMessage(pingComponent);
        }, 1, java.util.concurrent.TimeUnit.SECONDS);
    }
}