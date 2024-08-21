package net.vexmos.proxy.api;

import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.event.EventHandler;
import net.vexmos.database.bungee.ConnectBungee;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import java.util.HashSet;
import java.util.Set;

public class PermissionAPI implements Listener {

    private static PermissionAPI instance;

    private final BungeeConfig config = new BungeeConfig("permissions.yml");

    private final ConnectBungee connectBungee = new ConnectBungee();

    public static PermissionAPI getInstance() {
        if (instance == null)
            instance = new PermissionAPI();
        return instance;
    }

    public Set<String> getPermissions(ProxiedPlayer player) {
        Set<String> permissions = new HashSet<>();
        String group = getGroup(player);
        if (group != null && this.config.getConfig().getSection("permissions").contains(group))
            permissions.addAll(this.config.getConfig().getStringList("permissions." + group));
        return permissions;
    }

    public boolean hasPermission(ProxiedPlayer player, String permission) {
        return getPermissions(player).contains(permission);
    }

    public String getGroup(ProxiedPlayer player) {
        return this.connectBungee.getPlayerGroup(player.getName());
    }

    @EventHandler
    public void onJoin(ServerConnectedEvent event) {
        ProxiedPlayer player = event.getPlayer();
        onPlayerJoin(player);
    }

    public void onPlayerJoin(ProxiedPlayer player) {
        Set<String> currentPermissions = getPermissions(player);
        for (String permission : currentPermissions)
            player.setPermission(permission, false);
        Set<String> newPermissions = getPermissions(player);
        for (String permission : newPermissions)
            player.setPermission(permission, true);
    }

    public void reloadConfig() {
        this.config.reloadConfig();
    }

    public void setGroup(ProxiedPlayer player, String group) {
        this.connectBungee.setPlayerGroup(player.getName(), group);
    }

}