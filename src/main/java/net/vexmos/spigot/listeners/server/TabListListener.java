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
        String header = "§8\n" +  // Linha cinza escuro
                "§8\n" +  // Linha cinza escuro
                "          §6§lVEXMOS        \n" +  // Texto centralizado
                "§8\n" +  // Linha cinza escuro
                "  §eVocê está jogando em §6§lVEXMOS.NET \n" +  // Texto informativo
                "§8\n" +  // Linha cinza escuro
                "§8\n";  // Linha cinza escuro

        String footer = "§8\n" +  // Linha cinza escuro
                "§8\n" +  // Linha cinza escuro
                "  §eSeu ping: §a" + getPing(player) + "  \n" +  // Ping do jogador
                "  §eSala: §a" + Bukkit.getServer().getServerName() + "  \n" +  // Mundo do jogador
                "§8\n" +  // Linha cinza escuro
                "    §ewww.vexmos.net   \n" +  // Site
                "    §8discord.vexmos.net   \n" +  // Discord
                "    §8loja.vexmos.net   \n\n" +  // Loja
                "    §eRedes Sociais:   \n" +  // Loja
                "    §aIG: §b@VexmosMC  \n" +  // Instagram
                "    §aTikTok: §b@VexmosMC  \n" +  // Tiktok
                "§8\n" +  // Linha cinza escuro
                "§8\n" +  // Linha cinza escuro
                "  §7Vexmos© 2025  \n" +  // Direitos autorais
                "§8\n" +  // Linha cinza escuro
                "§8\n";  // Linha cinza escuro

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
