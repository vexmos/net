package net.vexmos.proxy.listeners;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.vexmos.proxy.api.PermissionAPI;

import java.util.Set;

public class PermissionListener implements Listener {

    @EventHandler
    public void onJoin(ServerConnectedEvent event) {
        ProxiedPlayer player = event.getPlayer();
        onPlayerJoin(player);
    }

    public void onPlayerJoin(ProxiedPlayer player) {
        Set<String> currentPermissions = PermissionAPI.getInstance().getPermissions(player);
        for (String permission : currentPermissions) {
            player.setPermission(permission, false);
        }
        Set<String> newPermissions = PermissionAPI.getInstance().getPermissions(player);
        for (String permission : newPermissions) {
            player.setPermission(permission, true);
        }
    }

}
