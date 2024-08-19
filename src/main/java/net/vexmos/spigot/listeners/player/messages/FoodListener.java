package net.vexmos.spigot.listeners.player.messages;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodListener implements Listener {

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent e) {
        Player foodChangedPlayer = (Player) e.getEntity();

        //Cancelling event, thus cancelling hunger
        foodChangedPlayer.setFoodLevel(20);
        e.setCancelled(true);
    }
}
