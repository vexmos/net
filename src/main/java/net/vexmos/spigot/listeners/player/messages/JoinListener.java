package net.vexmos.spigot.listeners.player.messages;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinListener implements Listener {

    @EventHandler
    private void joinMessage(PlayerJoinEvent e){
        e.setJoinMessage(null);
    }

	@EventHandler
	private void quitMessage(PlayerQuitEvent e) {
		e.setQuitMessage(null);
	}

}
