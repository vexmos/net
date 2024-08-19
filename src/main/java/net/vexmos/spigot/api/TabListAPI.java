package net.vexmos.spigot.api;

import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class TabListAPI {

    public static void sendTabList(Player player, String header, String footer) {
        IChatBaseComponent headerText = new ChatComponentText(header);
        IChatBaseComponent footerText = new ChatComponentText(footer);

        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter(headerText);

        try {
            Field footerField = packet.getClass().getDeclaredField("b");
            footerField.setAccessible(true);
            footerField.set(packet, footerText);

            // Enviar o pacote ao jogador
            CraftPlayer craftPlayer = (CraftPlayer) player;
            craftPlayer.getHandle().playerConnection.sendPacket((Packet<?>) packet);
        } catch (Exception e) {
            e.printStackTrace(); // Loga a exceção para depuração
        }
    }
}
