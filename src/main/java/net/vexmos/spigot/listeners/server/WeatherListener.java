package net.vexmos.spigot.listeners.server;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WeatherListener implements Listener {

    @EventHandler
    private void weatherDisable(WeatherChangeEvent e){
        e.setCancelled(true);
    }

}
