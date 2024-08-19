package net.vexmos.bungee.commands;

import net.vexmos.bungee.Bungee;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class StatsCommand extends Commands {

    public StatsCommand() {
        super("stats", "estatisticas", "statistics");
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new TextComponent("§cEste comando só pode ser usado por jogadores."));
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;

        // Enviar mensagem de checagem de informações
        player.sendMessage(new TextComponent("§aChecando suas informações..."));
        
        // Aguarda 1 segundo antes de enviar a próxima mensagem
        Bungee.get().getProxy().getScheduler().schedule(Bungee.get(), () -> {
            player.sendMessage(new TextComponent("§aSuas informações foram verificadas com sucesso."));
            
            // Mensagem clicável com link para o site
            TextComponent message = new TextComponent("§ePara verificar suas informações, você pode clicar ");
            TextComponent clickHere = new TextComponent("§b§lAQUI");

            clickHere.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aClique aqui para verificar suas informações.").create()));
            clickHere.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://lunikmc.com/stats/" + player.getUniqueId()));

            message.addExtra(clickHere);

            player.sendMessage(message);
        }, 1, java.util.concurrent.TimeUnit.SECONDS);
    }
}