package net.vexmos.proxy.configs;

import net.vexmos.proxy.api.BungeeConfig;
import net.vexmos.proxy.api.PermissionAPI;

public class PermissionsConfig {

    public static BungeeConfig permissions = new BungeeConfig("permissions.yml");

    public static void setup(){
        permissions.saveConfig();
        new PermissionAPI();
    }

}
