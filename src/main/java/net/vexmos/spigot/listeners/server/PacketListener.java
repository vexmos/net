package net.vexmos.spigot.listeners.server;

import net.vexmos.spigot.VexmosNET;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class PacketListener implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        // Verifica e bloqueia pacotes maliciosos em canais específicos
        if ("MC|Brand".equals(channel) || "REGISTER".equals(channel)) {
            VexmosNET.get().getLogger().warning("Bloqueou pacote potencialmente malicioso de " + player.getName() + " no canal: " + channel);
            player.kickPlayer("§cSua conexão foi interrompida.");
        }
    }
}
