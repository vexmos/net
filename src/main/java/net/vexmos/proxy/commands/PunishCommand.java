package net.vexmos.proxy.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.vexmos.database.bungee.ConnectBungee;

public class PunishCommand extends Commands {

    private final ConnectBungee database;

    public PunishCommand(ConnectBungee database) {
        super("ban", null, "banir", "punir", "punish");
        this.database = database;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new TextComponent("§cApenas jogadores podem usar esse comando."));
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;

        if (!player.hasPermission("grupo.diretor") && !player.hasPermission("grupo.mod") &&
                !player.hasPermission("grupo.dev") && !player.hasPermission("grupo.admin")) {
            player.sendMessage(new TextComponent("§cVocê não tem permissão para isto."));
            return;
        }

        if (args.length == 0) {
            player.sendMessage(new TextComponent("§6Uso: §c/ban <jogador>"));
            return;
        }

        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);

        if (target == null) {
            player.sendMessage(new TextComponent("§cJogador não encontrado."));
            return;
        }

        // Exibir razões clicáveis
        showPunishmentReasons(player, target);
    }

    private void showPunishmentReasons(ProxiedPlayer player, ProxiedPlayer target) {
        player.sendMessage(new TextComponent("\n §eEscolha a razão para a punição:"));

        // Exemplo de motivo: Uso de Hack
        TextComponent hackReason = new TextComponent(" §cUso de Hack §7[CLIQUE]");
        hackReason.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("§4[GRAVE]\n§7Modificações ilegais no cliente para obter vantagem desleal.\n\n§eTempo:\n§8- §cPermanente\n\n§aClique para aplicar punição.").create()));
        hackReason.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ban " + target.getName() + " Uso de Hack"));
        player.sendMessage(hackReason);

        // Exemplo de motivo: Uso de Exploits
        TextComponent exploitReason = new TextComponent(" §cUso de Exploits §7[CLIQUE]");
        exploitReason.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("§4[GRAVE]\n§7Aproveitamento de falhas ou bugs para ganho injusto ou causar prejuízo ao servidor.\n\n§eTempo:\n§8- §cPermanente\n\n§aClique para aplicar punição.").create()));
        exploitReason.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ban " + target.getName() + " Uso de Exploits"));
        player.sendMessage(exploitReason);

        // Outros motivos...
    }
}
