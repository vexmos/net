package net.vexmos.spigot.api;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class TitleAPI {

    /**
     * Envia uma mensagem de TitleAPI para um jogador específico.
     *
     * @param player  O jogador para quem enviar a mensagem.
     * @param title O titulo principal.
     * @param subtitle O subtitulo.
     * @param fadeIn O tempo para aparecer a mensagem em segundos.
     * @param stay O tempo para a mensagem ficar na tela em segundos.
     * @param fadeOut O tempo para desaparecer em segundos.
     */
    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;

        int fadeInTicks = fadeIn * 20;
        int stayTicks = stay * 20;
        int fadeOutTicks = fadeOut * 20;

        PacketPlayOutTitle packetTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE,
                IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + title + "\"}"), fadeInTicks, stayTicks, fadeOutTicks);
        PacketPlayOutTitle packetSubtitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE,
                IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + subtitle + "\"}"), fadeInTicks, stayTicks, fadeOutTicks);

        connection.sendPacket(packetTitle);
        connection.sendPacket(packetSubtitle);
    }

    /**
     * Envia uma mensagem de TitleAPI para todos os jogadores no servidor.
     *
     * @param title O titulo principal.
     * @param subtitle O subtitulo.
     * @param fadeIn O tempo para aparecer a mensagem em segundos.
     * @param stay O tempo para a mensagem ficar na tela em segundos.
     * @param fadeOut O tempo para desaparecer em segundos.
     */
    public static void broadcastTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            sendTitle(player, title, subtitle, fadeIn, stay, fadeOut);
        }
    }

        /**
     * Limpa os títulos e subtítulos de um jogador específico.
     *
     * @param player O jogador para quem limpar os títulos.
     */
    public static void clearTitle(Player player) {
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;

        PacketPlayOutTitle clearTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE,
                IChatBaseComponent.ChatSerializer.a("{\"text\":\"\"}"), 0, 0, 0);
        PacketPlayOutTitle clearSubtitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE,
                IChatBaseComponent.ChatSerializer.a("{\"text\":\"\"}"), 0, 0, 0);
        PacketPlayOutTitle clearTimes = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, null, 0, 0, 0);

        connection.sendPacket(clearTitle);
        connection.sendPacket(clearSubtitle);
        connection.sendPacket(clearTimes);
    }


}
