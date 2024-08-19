package net.vexmos.spigot.listeners.player.messages;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.entity.Player;
import net.vexmos.spigot.api.NameTagAPI;
import net.vexmos.database.spigot.ConnectSpigot;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatListener implements Listener {

    private static boolean chatEnabled = true;
    private static String lastDisabledTime = "";

    private ConnectSpigot connectSpigot = new ConnectSpigot();

    public static boolean isChatEnabled() {
        return chatEnabled;
    }

    public static String getLastDisabledTime() {
        return lastDisabledTime;
    }

    @EventHandler
    public void chatFormatter(AsyncPlayerChatEvent event) {

        Player player = event.getPlayer();

        // Verificar se o chat está habilitado
        if (!chatEnabled && !player.hasPermission("system.cmd.chat")) {
            event.setCancelled(true);

            TextComponent infoMessage = new TextComponent("§cO chat está desabilitado §8[INFO]");
            infoMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    new ComponentBuilder("§cO chat está desabilitado desde:\n §8- §e" + lastDisabledTime).create()));
            player.spigot().sendMessage(infoMessage);
            return;
        }

        String playerName = player.getName();
        String message = event.getMessage();

        String tag = connectSpigot.getPlayerTag(player.getName());
        if (tag != null) {
            playerName = getTagFormat(tag) + " " + playerName;
        } else {
            if (tag.equalsIgnoreCase("membro") || tag.equalsIgnoreCase("default")) {
                playerName = getTagFormat(tag) + "" + playerName;
            }
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String dateTime = sdf.format(new Date());

        TextComponent finalMessage = new TextComponent("§8[§2-§8] ");
        finalMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("§eRanking:\n§a- §2Unranked §8[§2-§8]").create()));

        TextComponent playerComponent = new TextComponent("§f" + playerName + " ");
        playerComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("§aClique aqui para mandar\n§auma mensagem privada.").create()));
        playerComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tell " + player.getName() + " "));

        TextComponent messageComponent = new TextComponent("§8› §f" + message);
        messageComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("§eMensagem enviada:\n§8- §f" + dateTime).create()));

        finalMessage.addExtra(playerComponent);
        finalMessage.addExtra(messageComponent);

        event.setCancelled(true);
        player.getServer().spigot().broadcast(finalMessage);
    }

    @EventHandler
    public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String tag = connectSpigot.getPlayerTag(player.getName());
        if (tag != null) {
            String tagFormat = getTagFormat(tag);
            String tagColor = getTagFormatColor(tag);
            NameTagAPI.setNametag(player, tagFormat + " ", tagColor);
        }
    }

    // Método para atualizar o estado do chat
    public static void setChatState(boolean enabled, String lastDisabledTime) {
        chatEnabled = enabled;
        if (!enabled) {
            ChatListener.lastDisabledTime = lastDisabledTime;
        }
    }

    private String getTagFormat(String tag) {
        switch (tag) {
            case "diretor": return "§4§lDIRETOR§4";
            case "admin": return "§c§lADMIN§c";
            case "dev": return "§3§lDEV§3";
            case "construtor": return "§e§lCONSTRUTOR§e";
            case "mod": return "§2§lMOD§2";
            case "suporte": return "§3§lSUPORTE§3";
            case "emerald": return "§a§lEMERALD§a";
            case "diamond": return "§b§lDIAMOND§b";
            case "gold": return "§6§lGOLD§6";
            case "membro": return "§7";
            case "default": return "§7";
            default: return null;
        }
    }


    private String getTagFormatColor(String tag) {
        switch (tag) {
            case "diretor": return "§4";
            case "admin": return "§c";
            case "dev": return "§3";
            case "construtor": return "§e";
            case "mod": return "§2";
            case "suporte": return "§3";
            case "emerald": return "§a";
            case "diamond": return "§b";
            case "gold": return "§6";
            case "membro": return "§7";
            case "default": return "§7";
            default: return null;
        }
    }
}