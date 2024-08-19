package net.vexmos.bungee.configs;

import net.vexmos.bungee.api.BungeeConfig;
import net.vexmos.bungee.api.PermissionAPI;

public class PermissionsConfig {

    public static BungeeConfig permissions = new BungeeConfig("permissions.yml");

    public static void setup(){
        permissions.saveConfig();
        new PermissionAPI();
    }

}
