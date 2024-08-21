package net.vexmos.spigot.listeners.player.messages;

import net.vexmos.spigot.VexmosNET;
import net.vexmos.spigot.api.BossBar;
import net.vexmos.spigot.api.TitleAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class TitlesListener implements Listener {

    public TitlesListener() {
        BossBar();
    }

    @EventHandler
    private void joinPlayers(PlayerJoinEvent e){
        Player player = e.getPlayer();
        if(Bukkit.getServerName().equals("Login")) {
            return;
        }
        TitleAPI.sendTitle(player, "§6§lVexmos","§7© 2025 Vexmos, Inc.", 1, 1, 1);
    }

    public void BossBar() {
        new BukkitRunnable() {
            private boolean toggle = true; // Variável para alternar entre as mensagens
            private final String message1 = "§6§lVexmos"; // Primeira mensagem
            private final String message2 = "§e§lVexmos"; // Segunda mensagem

            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    BossBar boss = new BossBar(player);
                    // Alternar a mensagem entre message1 e message2
                    if (toggle) {
                        boss.send(message1);
                    } else {
                        boss.send(message2);
                    }
                }
                toggle = !toggle; // Alternar o estado
            }
        }.runTaskTimer(VexmosNET.get(), 0L, 10L); // Começa imediatamente e repete a cada 20 ticks (1 segundo)
    }



}
