package net.vexmos.spigot.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class NameTagAPI {
    public static void setNametag(Player player, String prefix, String color) {
        // Cria uma nova scoreboard para o jogador



        // Define o nome na lista de jogadores
        String displayName = prefix + color + player.getName();
        player.setPlayerListName(displayName);
        player.setDisplayName(player.getName());


    }

}
