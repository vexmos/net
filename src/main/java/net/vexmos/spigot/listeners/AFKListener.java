
package net.vexmos.spigot.listeners;

import net.vexmos.spigot.VexmosNET;
import net.vexmos.spigot.api.SpigotConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AFKListener implements Listener {

    private SpigotConfig config;
    private Map<UUID, Long> lastMoveTimes;
    private int cooldown;
    private boolean enabled;

    public AFKListener() {
        config = new SpigotConfig("afk.yml");
        config.saveDefaultConfig();
        lastMoveTimes = new HashMap<>();
        cooldown = config.getConfig().getInt("cooldown", 55);
        enabled = config.getConfig().getBoolean("enabled", true);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        lastMoveTimes.put(player.getUniqueId(), System.currentTimeMillis());
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        lastMoveTimes.put(player.getUniqueId(), System.currentTimeMillis());
    }

    public void checkAFK() {
        if (!enabled) return;
        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            long lastMoveTime = lastMoveTimes.getOrDefault(uuid, 0L);
            long currentTime = System.currentTimeMillis();
            long diff = currentTime - lastMoveTime;

            if (diff >= (cooldown - 6) * 1000) {
                player.sendMessage("§cVocê vai ser §4§lEXPULSO §cpor estar §6§lAFK§c, mova-se!!");
            }

            if (diff >= cooldown * 1000) {
                player.kickPlayer("§cDormiu no teclado, hein? :)");
            }
        }
    }

    public void start() {
        new BukkitRunnable() {
            @Override
            public void run() {
                checkAFK();
            }
        }.runTaskTimer(VexmosNET.get(), 0, 20); // Run every 20 ticks (1 second)
    }
}


