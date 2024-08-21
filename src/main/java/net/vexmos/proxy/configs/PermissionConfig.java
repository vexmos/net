package net.vexmos.proxy.configs;

import net.vexmos.proxy.api.BungeeConfig;
import net.vexmos.proxy.api.PermissionAPI;
import net.vexmos.proxy.api.TwitterAPI;

public class PermissionConfig {

    public static BungeeConfig perm = new BungeeConfig("permissions.yml");

    public static void setup(){
        new PermissionAPI();
        perm.saveDefault();
        perm.saveDefaultConfig();
    }

}
