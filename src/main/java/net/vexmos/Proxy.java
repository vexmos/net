package net.vexmos;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.vexmos.proxy.configs.Configs;
import net.vexmos.proxy.database.ProxyConnection;

public class Proxy extends Plugin {

    @Override
    public void onEnable() {
        new Configs();
        new ProxyConnection();
    }

    @Override
    public void onDisable() {
    }

    public static Proxy get(){
        return (Proxy) ProxyServer.getInstance().getPluginManager().getPlugin("VexmosNet");
    }

}
