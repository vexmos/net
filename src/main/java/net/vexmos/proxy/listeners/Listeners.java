package net.vexmos.proxy.listeners;

import net.vexmos.proxy.VexmosPROXY;
import net.vexmos.proxy.api.PermissionAPI;

public class Listeners {

    public Listeners(){
        VexmosPROXY.get().getProxy().getPluginManager().registerListener(VexmosPROXY.get(), new MotdListener());
        VexmosPROXY.get().getProxy().getPluginManager().registerListener(VexmosPROXY.get(), new PermissionAPI());
    }

}
