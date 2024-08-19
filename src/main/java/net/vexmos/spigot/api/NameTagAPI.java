package net.vexmos.spigot.api;

import org.bukkit.entity.Player;

public class NameTagAPI {

    public static void setNametag(Player player, String prefix, String color) {
        String displayName = prefix + color + player.getName();

        player.setPlayerListName(displayName);
        player.setDisplayName(displayName);

    }
}
