package net.vexmos.spigot.listeners.player.messages;

import net.vexmos.spigot.api.TitleAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class TitlesListener implements Listener {

    @EventHandler
    private void joinPlayers(PlayerJoinEvent e){
        Player player = e.getPlayer();
        TitleAPI.sendTitle(player, "§6§lVexmos","§7© 2025 Vexmos, Inc.", 1, 1, 1);
    }

}
