package net.vexmos.spigot.api;

import net.vexmos.spigot.Services;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class PermissionConfig {

    private static SpigotConfig config;

    public PermissionConfig() {
        config = new SpigotConfig("permissions.yml");
        config.saveDefaultConfig();
    }

    public static void removeAllPermissionsFromPlayer(Player player) {
        PermissionAttachment attachment = player.addAttachment(Services.get());
        Map<String, Boolean> permissions = attachment.getPermissions();
        Set<String> permKeys = permissions.keySet();
        for (String perm : permKeys) {
            player.removeAttachment(attachment);
        }
    }

    public static List<String> getPermissions(String group) {
        FileConfiguration fileConfig = config.getConfig();
        return fileConfig.getStringList("permissions." + group);
    }
}
