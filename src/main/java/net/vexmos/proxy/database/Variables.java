package net.vexmos.proxy.database;

import net.vexmos.proxy.configs.ProxyConfigs;

public class Variables {

    private static ProxyConfigs database = new ProxyConfigs("database.yml");

    public static final String host = getDatabaseConfig("mysql.host");
    public static final String name = getDatabaseConfig("mysql.name");
    public static final String username = getDatabaseConfig("mysql.username");
    public static final String password = getDatabaseConfig("mysql.password");
    public static final int port = getDatabaseConfigInt("mysql.port");

    private static String getDatabaseConfig(String path) {
        return database.getConfig().getString(path);
    }

    private static int getDatabaseConfigInt(String path) {
        return database.getConfig().getInt(path);
    }
}
