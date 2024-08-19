package net.vexmos.spigot.listeners.player.gamerules;

import org.bukkit.Bukkit;
import org.bukkit.World;

public class DoDayLightCycle {

    public static void setup() {
        for (World world : Bukkit.getWorlds()) {
            world.setGameRuleValue("doDayLightCycle", "false");
            world.setTime(1000);
        }
    }

}
