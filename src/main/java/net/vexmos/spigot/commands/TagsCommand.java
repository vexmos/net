package net.vexmos.spigot.commands;

import net.vexmos.database.spigot.ConnectSpigot;
import net.vexmos.spigot.api.NameTagAPI;
import net.vexmos.spigot.api.PermissionConfig;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TagsCommand extends Commands {

    private static ConnectSpigot database;
    private static PermissionConfig permissionConfig;

    private static final List<String> TAGS_DIRETOR = Arrays.asList("diretor", "admin", "dev", "construtor", "mod", "suporte", "emerald", "diamond", "gold", "membro", "default");
    private static final List<String> TAGS_ADMIN = Arrays.asList("admin", "dev", "construtor", "mod", "suporte", "emerald", "diamond", "gold", "membro", "default");
    private static final List<String> TAGS_DEV = Arrays.asList("dev", "construtor", "mod", "suporte", "emerald", "diamond", "gold", "membro", "default");
    private static final List<String> TAGS_CONSTRUTOR = Arrays.asList("construtor", "mod", "suporte", "emerald", "diamond", "gold", "membro", "default");
    private static final List<String> TAGS_MOD = Arrays.asList("mod", "suporte", "emerald", "diamond", "gold", "membro", "default");
    private static final List<String> TAGS_SUPORTE = Arrays.asList("suporte", "emerald", "diamond", "gold", "membro", "default");
    private static final List<String> TAGS_EMERALD = Arrays.asList("emerald", "diamond", "gold", "membro", "default");
    private static final List<String> TAGS_DIAMOND = Arrays.asList("diamond", "gold", "membro", "default");
    private static final List<String> TAGS_GOLD = Arrays.asList("gold", "membro", "default");
    private static final List<String> TAGS_MEMBRO = Arrays.asList("membro", "default");

    public TagsCommand() {

        super("tag", "tags");  // Register both /tag and /tags
    }


    /*
    public void updatePlayerGroup(Player player) {
        String playerName = player.getName();
        String group = database.getPlayerGroup(playerName);

        if (group == null) {
            Bukkit.getLogger().warning("Group for player " + playerName + " is null.");
            return;
        }
        List<String> permissions = PermissionConfig.getPermissions(group);
        String firstPermission = permissions.get(0);
        if (player.hasPermission(firstPermission)) {
            return;
        }

        PermissionConfig.removeAllPermissionsFromPlayer(player);
        if (permissions == null) {
            Bukkit.getLogger().warning("Permissions list for group " + group + " is null.");
            return;
        }
        for (String permission : permissions) {
            player.addAttachment(Spigot.get(), permission, true);
        }
    }
     */

    @Override
    public void perform(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cApenas jogadores podem usar este comando.");
            return;
        }

        Player player = (Player) sender;

        // Handle /tags
        if (label.equalsIgnoreCase("tags")) {
            sendTagsMenu(player);
            return;
        }

        // Handle /tag
        if (args.length < 1) {
            player.sendMessage("§cUse: /tag <tag>");
            return;
        }

        String tag = args[0].toLowerCase();
        String tagFormat = getTagFormat(tag);
        String tagColor = getTagFormatColor(tag);
        ConnectSpigot connectSpigot = new ConnectSpigot();



        if (tagFormat == null || tagColor == null) {
            player.sendMessage("§cTag inválida.");
            return;
        }
        if (tag.equals(connectSpigot.getPlayerTag(player.getName()))) {
            player.sendMessage("§cVocê já está usando a tag " + tagFormat);
            return;
        }

        if (tag.equals("default")) {
            if(connectSpigot.getPlayerTag(player.getName()).equalsIgnoreCase("membro")) {
                player.sendMessage("§cVocê já está usando a tag " + tagFormat);
                return;
            }
        }
        if (tag.equals("membro")) {
            if(connectSpigot.getPlayerTag(player.getName()).equalsIgnoreCase("default")) {
                player.sendMessage("§cVocê já está usando a tag " + tagFormat);
                return;
            }
        }

        // Set the player's tag using NameTagAPI (this is pseudo-code, replace it with your actual API method)
        if (tag.equals("membro") || tag.equals("default")) {
            NameTagAPI.setNametag(player, "§7" + " ", tagColor);
        } else {
            NameTagAPI.setNametag(player, tagFormat + " ", tagColor);
        }
        player.sendMessage("§aVocê selecionou a tag " + tagFormat);
        connectSpigot.setPlayerTag(player.getName(), tag);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        if (args.length == 1) {
            List<String> completions = new ArrayList<>();
            List<String> tags = getTagsForPlayer(sender);
            StringUtil.copyPartialMatches(args[0], tags, completions);
            return completions;
        }
        return new ArrayList<>();
    }

    private List<String> getTagsForPlayer(CommandSender sender) {
        if (sender.hasPermission("group.diretor")) {
            return TAGS_DIRETOR;
        } else if (sender.hasPermission("group.admin")) {
            return TAGS_ADMIN;
        } else if (sender.hasPermission("group.dev")) {
            return TAGS_DEV;
        } else if (sender.hasPermission("group.construtor")) {
            return TAGS_CONSTRUTOR;
        } else if (sender.hasPermission("group.mod")) {
            return TAGS_MOD;
        } else if (sender.hasPermission("group.suporte")) {
            return TAGS_SUPORTE;
        } else if (sender.hasPermission("group.emerald")) {
            return TAGS_EMERALD;
        } else if (sender.hasPermission("group.diamond")) {
            return TAGS_DIAMOND;
        } else if (sender.hasPermission("group.gold")) {
            return TAGS_GOLD;
        } else if (sender.hasPermission("group.membro")) {
            return TAGS_MEMBRO;
        } else {
            return TAGS_MEMBRO;
        }
    }

    private void sendTagsMenu(Player player) {
        player.sendMessage("§eSelecione a tag:");
        for (String tag : getTagsForPlayer(player)) {
            String tagFormat = getTagFormat(tag);

            TextComponent message = new TextComponent("§e- " + tagFormat);
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§eClique para selecionar a tag " + tagFormat).create()));
            message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tag " + tag));

            player.spigot().sendMessage(message);
        }
    }

    private String getTagFormat(String tag) {
        switch (tag) {
            case "diretor": return "§4§lDIRETOR";
            case "admin": return "§c§lADMIN";
            case "dev": return "§3§lDEV";
            case "construtor": return "§e§lCONSTRUTOR";
            case "mod": return "§2§lMOD";
            case "suporte": return "§3§lSUPORTE";
            case "emerald": return "§a§lEMERALD";
            case "diamond": return "§b§lDIAMOND";
            case "gold": return "§6§lGOLD";
            case "membro": return "§7Membro";
            case "default": return "§7Default";
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
