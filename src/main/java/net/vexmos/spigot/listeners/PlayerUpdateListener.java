package net.vexmos.spigot.listeners;

import net.vexmos.database.spigot.ConnectSpigot;
import net.vexmos.spigot.VexmosNET;
import net.vexmos.spigot.api.BossBarAPI;
import net.vexmos.spigot.api.PermissionConfig;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PlayerUpdateListener implements Listener {

    private static ConnectSpigot database;
    private static PermissionConfig permissionConfig;
    private BossBarAPI bossBarAPI;
    private final Map<Player, LinkedList<String>> playerMessages = new HashMap<>(); // To store last messages

    public PlayerUpdateListener(JavaPlugin plugin, ConnectSpigot database) {
        this.database = database;
        permissionConfig = permissionConfig;
        this.permissionConfig = new PermissionConfig();
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        updatePlayerGroup(event.getPlayer());
        String group = database.getPlayerGroup(p.getName());
        String tag = database.getPlayerTag(p.getName());
        if(tag == null) {
            database.setPlayerTag(p.getName(), group);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        updatePlayerGroup(player);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        updatePlayerGroup(player);
    }






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
            player.addAttachment(VexmosNET.get(), permission, true);
        }


    }
}
