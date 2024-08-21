package net.vexmos.proxy.configs;

public class Configs {

    private ProxyConfigs database = new ProxyConfigs("database.yml");

    public Configs(){
        database.reloadConfig();
    }

}
