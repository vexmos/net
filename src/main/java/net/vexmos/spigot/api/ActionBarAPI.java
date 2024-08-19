package net.vexmos.spigot.api;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class ActionBarAPI {

    /**
     * Envia uma mensagem de ActionBar para um jogador específico.
     *
     * @param player  O jogador para quem enviar a mensagem.
     * @param message A mensagem a ser enviada.
     */
    public static void sendActionBar(Player player, String message) {
        IChatBaseComponent chatComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
        PacketPlayOutChat packet = new PacketPlayOutChat(chatComponent, (byte) 2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    /**
     * Envia uma mensagem de ActionBar para todos os jogadores no servidor.
     *
     * @param message A mensagem a ser enviada.
     */
    public static void broadcastActionBar(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            sendActionBar(player, message);
        }
    }

}
