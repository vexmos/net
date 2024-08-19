package net.vexmos.spigot.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class NameTagAPI {

    public static void setNametag(Player player, String prefix, String color) {
        String displayName = prefix + color + player.getName();

        player.setPlayerListName(displayName);
        player.setDisplayName(displayName);

        for (Player online : Bukkit.getOnlinePlayers()) {
            sendPacket(online, createNameTagPacket(player, displayName));
        }
    }

    private static void sendPacket(Player player, Object packet) {
        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            Method sendPacketMethod = playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet"));
            sendPacketMethod.invoke(playerConnection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Object createNameTagPacket(Player player, String nameTag) {
        try {
            Class<?> packetClass = getNMSClass("PacketPlayOutPlayerInfo");
            Class<?> playerInfoDataClass = getNMSClass("PacketPlayOutPlayerInfo$PlayerInfoData");
            Class<?> gameProfileClass = getNMSClass("GameProfile");
            Class<?> enumGamemodeClass = getNMSClass("EnumGamemode");
            Class<?> enumPlayerInfoActionClass = getNMSClass("PacketPlayOutPlayerInfo$EnumPlayerInfoAction");
            Class<?> chatSerializerClass = getNMSClass("ChatSerializer");
            Class<?> chatComponentClass = getNMSClass("IChatBaseComponent");

            Constructor<?> gameProfileConstructor = gameProfileClass.getConstructor(UUID.class, String.class);
            Object gameProfile = gameProfileConstructor.newInstance(player.getUniqueId(), player.getName());

            Constructor<?> playerInfoDataConstructor = playerInfoDataClass.getConstructor(
                    gameProfileClass, int.class, enumGamemodeClass, chatComponentClass
            );
            Object chatComponent = chatSerializerClass.getMethod("a", String.class).invoke(null, "{\"text\":\"" + nameTag + "\"}");
            Object playerInfoData = playerInfoDataConstructor.newInstance(gameProfile, 0, enumGamemodeClass.getField("SURVIVAL").get(null), chatComponent);

            Constructor<?> packetConstructor = packetClass.getConstructor(
                    enumPlayerInfoActionClass, List.class
            );
            Object packet = packetConstructor.newInstance(
                    enumPlayerInfoActionClass.getField("ADD_PLAYER").get(null),
                    Collections.singletonList(playerInfoData)
            );

            return packet;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server.v1_8_R3." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
