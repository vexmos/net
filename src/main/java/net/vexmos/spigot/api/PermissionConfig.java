package net.vexmos.spigot.api;

import net.vexmos.spigot.VexmosNET;
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
        config.saveDefault();
    }

    public static void removeAllPermissionsFromPlayer(Player player) {
        PermissionAttachment attachment = player.addAttachment(VexmosNET.get());
        Map<String, Boolean> permissions = attachment.getPermissions();

        // Iterate through the permissions and set each to false
        for (String perm : permissions.keySet()) {
            attachment.setPermission(perm, false);
        }

        // Remove the attachment after all permissions are set to false
        player.removeAttachment(attachment);
    }

    public static List<String> getPermissions(String group) {
        FileConfiguration fileConfig = config.getConfig();
        return fileConfig.getStringList("permissions." + group);
    }
}
