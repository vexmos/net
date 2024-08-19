package net.vexmos.spigot.listeners;

import net.vexmos.database.spigot.ConnectSpigot;
import net.vexmos.spigot.Services;
import net.vexmos.spigot.listeners.player.messages.*;
import net.vexmos.spigot.listeners.server.ExploitPreventionListener;
import net.vexmos.spigot.listeners.server.PacketListener;
import net.vexmos.spigot.listeners.server.TabListListener;
import net.vexmos.spigot.listeners.server.WeatherListener;
import org.bukkit.event.Listener;

public class Listeners implements Listener {

    public static void setup() {
        Services.get().getServer().getPluginManager().registerEvents(new TitlesListener(), Services.get());
        Services.get().getServer().getPluginManager().registerEvents(new WeatherListener(), Services.get());
        Services.get().getServer().getPluginManager().registerEvents(new JoinListener(), Services.get());
        Services.get().getServer().getPluginManager().registerEvents(new TabListListener(), Services.get());
        Services.get().getServer().getPluginManager().registerEvents(new DeathMessage(), Services.get());
        ConnectSpigot database = new ConnectSpigot();
        //Spigot.get().getServer().getPluginManager().registerEvents(new ExcludeListener(), Spigot.get());
        // Criar uma instância do ChatListener
        ChatListener chatListener = new ChatListener();
        Services.get().getServer().getPluginManager().registerEvents(chatListener, Services.get());

        Services.get().getServer().getPluginManager().registerEvents(new PlayerUpdateListener(Services.get(), new ConnectSpigot()), Services.get());

        // Registrar outros listeners
        Services.get().getServer().getPluginManager().registerEvents(new ExploitPreventionListener(), Services.get());
        Services.get().getServer().getPluginManager().registerEvents(new FoodListener(), Services.get());
        //Spigot.get().getServer().getPluginManager().registerEvents(new AFKListener(), Spigot.get());


        // Registrar PacketListener
        Services.get().getServer().getMessenger().registerIncomingPluginChannel(Services.get(), "COMMENT", new PacketListener());
    }
}
