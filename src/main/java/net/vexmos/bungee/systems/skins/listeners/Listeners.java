//package net.vexmos.bungee.systems.skins.listeners;
//
//import com.google.common.io.ByteArrayDataInput;
//import com.google.common.io.ByteStreams;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//import dev.redelegends.skins.Language;
//import dev.redelegends.skins.bungee.Main;
//import dev.redelegends.skins.plugin.CooldownStorage;
//import net.md_5.bungee.api.ProxyServer;
//import net.md_5.bungee.api.chat.TextComponent;
//import net.md_5.bungee.api.connection.PendingConnection;
//import net.md_5.bungee.api.connection.ProxiedPlayer;
//import net.md_5.bungee.api.connection.Server;
//import net.md_5.bungee.api.event.ChatEvent;
//import net.md_5.bungee.api.event.LoginEvent;
//import net.md_5.bungee.api.event.PlayerDisconnectEvent;
//import net.md_5.bungee.api.event.PluginMessageEvent;
//import net.md_5.bungee.api.plugin.Listener;
//import net.md_5.bungee.connection.InitialHandler;
//import net.md_5.bungee.connection.LoginResult;
//import net.md_5.bungee.event.EventHandler;
//import net.md_5.bungee.protocol.Property;
//import dev.redelegends.skins.bungee.api.SkinMethods;
//import dev.redelegends.skins.bungee.utils.JsonMessage;
//import dev.redelegends.skins.plugin.reflection.Accessors;
//
//public class Listeners implements Listener {
//    private static List<UUID> nicking = new ArrayList();
//
//    public static void makeListeners() {
//        ProxyServer.getInstance().getPluginManager().registerListener(Main.getInstance(), new Listeners());
//    }
//
//    public static void setNicking(ProxiedPlayer player) {
//        nicking.add(player.getUniqueId());
//    }
//
//    public static boolean isNicking(ProxiedPlayer player) {
//        return nicking.contains(player.getUniqueId());
//    }
//
//    @EventHandler
//    public void onPlayerQuit(PlayerDisconnectEvent evt) {
//        nicking.remove(evt.getPlayer().getUniqueId());
//    }
//
//    @EventHandler
//    public void onLogin(LoginEvent evt) {
//        PendingConnection connection = evt.getConnection();
//        InitialHandler handler = (InitialHandler)connection;
//        Property prop = SkinMethods.getOrCreateSkinForPlayer(connection.getName(), connection.getUniqueId().toString());
//        if (handler.isOnlineMode()) {
//            handler.getLoginProfile().setProperties(new Property[]{prop});
//        } else {
//            LoginResult result = null;
//
//            try {
//                result = (LoginResult)Accessors.getConstructor(LoginResult.class, String.class, String.class, Property[].class).newInstance(connection.getUniqueId().toString(), connection.getName(), new Property[]{prop});
//            } catch (Exception var7) {
//                result = (LoginResult)Accessors.getConstructor(LoginResult.class, String.class, Property[].class).newInstance(connection.getUniqueId().toString(), connection.getName());
//            }
//
//            Accessors.getField(InitialHandler.class, 0, LoginResult.class).set(handler, result);
//        }
//    }
//
//    @EventHandler(
//            priority = 64
//    )
//    public void onPlayerChat(ChatEvent evt) {
//        if (evt.getSender() instanceof ProxiedPlayer) {
//            ProxiedPlayer player = (ProxiedPlayer)evt.getSender();
//            String msg = evt.getMessage();
//            if (isNicking(player)) {
//                evt.setCancelled(true);
//                if (!evt.isCommand()) {
//                    nicking.remove(player.getUniqueId());
//                    if (msg.equals("event.cancel_skin")) {
//                        player.sendMessage(TextComponent.fromLegacyText(Language.messages$command$skin$event_cancelled));
//                        return;
//                    }
//
//                    ProxyServer.getInstance().getPluginManager().dispatchCommand(player, "skin " + msg);
//                    return;
//                }
//
//                JsonMessage.send(player, Language.messages$command$skin$event);
//            }
//
//            if (msg.equals("event.cancel_skin")) {
//                evt.setCancelled(true);
//            }
//        }
//
//    }
//
//    @EventHandler
//    public void onPluginMessage(PluginMessageEvent evt) {
//        if (evt.getTag().equals("LegendsSkins") && evt.getSender() instanceof Server && evt.getReceiver() instanceof ProxiedPlayer) {
//            ProxiedPlayer player = (ProxiedPlayer)evt.getReceiver();
//            ByteArrayDataInput in = ByteStreams.newDataInput(evt.getData());
//            String subChannel = in.readUTF();
//            if (subChannel.equals("SkinSet")) {
//                if (!player.hasPermission("legendsskins.bypass.cooldown")) {
//                    String cooldown = CooldownStorage.getCooldown(player.getName());
//                    if (!cooldown.isEmpty()) {
//                        player.sendMessage(TextComponent.fromLegacyText(Language.messages$command$skin$cooldown.replace("{time}", cooldown)));
//                        return;
//                    }
//                }
//
//                if (!player.hasPermission("legendsskins.cmd.skin")) {
//                    JsonMessage.send(player, Language.messages$command$skin$no_perm);
//                    return;
//                }
//
//                setNicking(player);
//                JsonMessage.send(player, Language.messages$command$skin$event);
//            } else if (subChannel.equals("SkinUpdate")) {
//                ProxyServer.getInstance().getPluginManager().dispatchCommand(player, "skin atualizar");
//            } else if (subChannel.equals("SkinHelp")) {
//                ProxyServer.getInstance().getPluginManager().dispatchCommand(player, "skin ajuda");
//            }
//        }
//
//    }
//}