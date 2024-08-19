package net.vexmos.spigot.listeners.server;

import net.vexmos.spigot.api.TabListAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class TabListListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        setTabList(player);
    }

    private void setTabList(Player player) {
        String header =
                "\n\n" + "§a§lVEXMOS" +
                "\n" + "§7     www.vexmos.net" +
                "\n\n";

        String footer =
                "\n\n" + "§bTikTok: §7@VexmosMC" +
                "\n" + "§bInstagram: §7@VexmosMC" +
                "\n" + "§bTwitter: §7@VexmosMC" +
                "\n\n" + "§eNossa Comunidade: §adiscord.vexmos.net" +
                "\n" + "                         §eAdquira VIP'S em: §aloja.vexmos.net                         " +
                "\n\n";

        // Enviar a tablist personalizada para o jogador
        TabListAPI.sendTabList(player, header, footer);
    }

    // Método auxiliar para obter o ping do jogador
    private String getPing(Player player) {
        // Substitua com a implementação apropriada para obter o ping
        // O ping pode ser obtido usando métodos NMS ou bibliotecas externas.
        // Exemplo genérico (você pode precisar ajustar para sua versão):
        return Integer.toString(((org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer) player).getHandle().ping);
    }
}
