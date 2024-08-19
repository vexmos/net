package net.vexmos.bungee.listeners;

import net.vexmos.bungee.Bungee;
import net.vexmos.bungee.api.PermissionAPI;

public class Listeners {

    public Listeners(){
        Bungee.get().getProxy().getPluginManager().registerListener(Bungee.get(), new MotdListener());
        Bungee.get().getProxy().getPluginManager().registerListener(Bungee.get(), new PermissionListener());
    }

}
