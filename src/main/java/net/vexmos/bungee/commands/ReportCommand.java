package net.vexmos.bungee.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ClickEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ReportCommand extends Commands {

    private final HashMap<String, Long> cooldowns = new HashMap<>();

    public ReportCommand() {
        super("report", "reportar", "denunciar");
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
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new TextComponent("command not allowed for console"));
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;
        String playerName = player.getName();
        String serverName = player.getServer().getInfo().getName();
    

        if (args.length == 0) {
            player.sendMessage(new TextComponent(ChatColor.GOLD + "Uso do " + ChatColor.GREEN + "/report" + ChatColor.GOLD + ":"));
            player.sendMessage(new TextComponent(ChatColor.YELLOW + "/report <jogador> <motivo>"));
            return;
        }

        if (args.length == 1) {
            player.sendMessage(new TextComponent(ChatColor.RED + "Uso: /report <jogador> <motivo>"));
            return;
        }

        String target = args[0];
        String reason = joinArgs(args, 1);

        if (isInCooldown(playerName)) {
            player.sendMessage(new TextComponent(ChatColor.RED + "Aguarde um pouco para fazer isto novamente."));
            return;
        }

        setCooldown(playerName);

        ProxiedPlayer reportedPlayer = ProxyServer.getInstance().getPlayer(target);
        String reportedServerName = (reportedPlayer != null) ? reportedPlayer.getServer().getInfo().getName() : "Desconhecido";

        player.sendMessage(new TextComponent(ChatColor.GREEN + "Você denunciou " + target + " por " + reason));

        for (ProxiedPlayer mod : ProxyServer.getInstance().getPlayers()) {
            if (hasRequiredPermission(mod)) {
                mod.sendMessage(new TextComponent(""));
                mod.sendMessage(new TextComponent(ChatColor.RED + "Reportado: " + target));
                mod.sendMessage(new TextComponent(ChatColor.GOLD + "Motivo: " + ChatColor.WHITE + reason));
                mod.sendMessage(new TextComponent(ChatColor.YELLOW + "Sala: " + ChatColor.WHITE + reportedServerName));
                mod.sendMessage(new TextComponent(ChatColor.GREEN + "Quem enviou: " + ChatColor.WHITE + playerName));
                mod.sendMessage(new TextComponent(""));

                TextComponent clickHere = new TextComponent(ChatColor.YELLOW + "Para se teleportar: " + ChatColor.AQUA + ChatColor.BOLD.toString() + "CLIQUE AQUI");
                clickHere.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/go " + target));
                mod.sendMessage(clickHere);

                mod.sendMessage(new TextComponent(""));
            }
        }
    }

    // cooldown check
    private boolean isInCooldown(String playerName) {
        if (cooldowns.containsKey(playerName)) {
            long lastUsed = cooldowns.get(playerName);
            return (System.currentTimeMillis() - lastUsed) < 5000;
        }
        return false;
    }

    // cooldown set
    private void setCooldown(String playerName) {
        cooldowns.put(playerName, System.currentTimeMillis());
    }

    // args string builder
    private String joinArgs(String[] args, int start) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < args.length; i++) {
            if (i != start) sb.append(" ");
            sb.append(args[i]);
        }
        return sb.toString();
    }

}
