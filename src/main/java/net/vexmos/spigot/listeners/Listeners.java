package net.vexmos.spigot.listeners;

import net.vexmos.database.spigot.ConnectSpigot;
import net.vexmos.spigot.VexmosNET;
import net.vexmos.spigot.listeners.player.messages.*;
import net.vexmos.spigot.listeners.server.ExploitPreventionListener;
import net.vexmos.spigot.listeners.server.PacketListener;
import net.vexmos.spigot.listeners.server.TabListListener;
import net.vexmos.spigot.listeners.server.WeatherListener;
import org.bukkit.event.Listener;

public class Listeners implements Listener {

    public static void setup() {
        VexmosNET.get().getServer().getPluginManager().registerEvents(new TitlesListener(), VexmosNET.get());
        VexmosNET.get().getServer().getPluginManager().registerEvents(new WeatherListener(), VexmosNET.get());
        VexmosNET.get().getServer().getPluginManager().registerEvents(new JoinListener(), VexmosNET.get());
        VexmosNET.get().getServer().getPluginManager().registerEvents(new TabListListener(), VexmosNET.get());
        VexmosNET.get().getServer().getPluginManager().registerEvents(new DeathMessage(), VexmosNET.get());
        ConnectSpigot database = new ConnectSpigot();
        // Criar uma instância do ChatListener
        ChatListener chatListener = new ChatListener();
        VexmosNET.get().getServer().getPluginManager().registerEvents(chatListener, VexmosNET.get());

        VexmosNET.get().getServer().getPluginManager().registerEvents(new PlayerUpdateListener(VexmosNET.get(), new ConnectSpigot()), VexmosNET.get());

        // Registrar outros listeners
        VexmosNET.get().getServer().getPluginManager().registerEvents(new ExploitPreventionListener(), VexmosNET.get());
        VexmosNET.get().getServer().getPluginManager().registerEvents(new FoodListener(), VexmosNET.get());

        //Spigot.get().getServer().getPluginManager().registerEvents(new AFKListener(), Spigot.get());


        // Registrar PacketListener
        VexmosNET.get().getServer().getMessenger().registerIncomingPluginChannel(VexmosNET.get(), "COMMENT", new PacketListener());
    }
}
