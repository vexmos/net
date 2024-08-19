//package net.vexmos.bungee.systems.skins.api;
//
//import java.sql.SQLException;
//import java.util.UUID;
//import java.util.logging.Level;
//import javax.sql.rowset.CachedRowSet;
//
//import dev.redelegends.skins.Core;
//import dev.redelegends.skins.Language;
//import dev.redelegends.skins.plugin.mojang.Mojang;
//import net.md_5.bungee.api.ProxyServer;
//import net.md_5.bungee.protocol.Property;
//import dev.redelegends.skins.bungee.database.Database;
//import dev.redelegends.skins.plugin.mojang.api.InvalidMojangException;
//import dev.redelegends.skins.plugin.utils.LegendsLogger;
//
//public class SkinMethods {
//    public static final LegendsLogger LOGGER;
//
//    public static Property createProperty(String name, String value, String signature) {
//        return new Property(name, value, signature);
//    }
//
//    public static void setSlot(String id, int slot) {
//        Database.getInstance().execute("UPDATE `playerskin` SET `slot` = ? WHERE `id` = ?", slot, id);
//        Database.getInstance().execute("UPDATE `playerskins` SET `lastused` = ? WHERE `owner` = ? AND `id` = ?", System.currentTimeMillis(), id, slot);
//    }
//
//    public static void tryUpdateOriginalSkin(String name, String id) {
//        try {
//            String uid = Mojang.getUUID(name);
//            if (uid != null) {
//                String uprop = Mojang.getSkinProperty(uid);
//                if (uprop != null) {
//                    Database.getInstance().execute("UPDATE `playerskins` SET `value` = ?, `signature` = ? WHERE `owner` = ? AND `id` = ?", uprop.split(" : ")[1], uprop.split(" : ")[2], id, 0);
//                }
//            }
//        } catch (InvalidMojangException var4) {
//            SkinQueue.addQueue(name, id);
//        }
//
//    }
//
//    public static Integer insertSkin(String player, String skinName, String id, String property, int currentId) {
//        CachedRowSet rs = Database.getInstance().query("SELECT * FROM `playerskins` WHERE `player` = ? AND `owner` = ?", id, player);
//        if (rs != null) {
//            try {
//                Database.getInstance().execute("UPDATE `playerskins` SET `value` = ?, `signature` = ? WHERE `player` = ? AND `owner` = ?", property.split(" : ")[1], property.split(" : ")[2], id, player);
//                return rs.getInt("id");
//            } catch (SQLException var7) {
//                LOGGER.log(Level.WARNING, "insertSkin(\"" + player + "\", \"" + skinName + "\", \"" + id + "\", \"" + currentId + "\"): ", var7);
//                return null;
//            }
//        } else {
//            Database.getInstance().execute("INSERT INTO `playerskins` VALUES (?, ?, ?, ?, ?, ?, ?)", currentId, player, id, skinName, property.split(" : ")[1], property.split(" : ")[2], System.currentTimeMillis());
//            return null;
//        }
//    }
//
//    public static void removeSkin(int id, String player) {
//        CachedRowSet rs = Database.getInstance().query("SELECT * FROM `playerskins` WHERE `id` = ? AND `owner` = ?", id, player);
//        int currentSlot = getSlot(player);
//        if (currentSlot == id) {
//            setSlot(player, 0);
//        }
//
//        if (rs != null) {
//            try {
//                Database.getInstance().execute("DELETE FROM `playerskins` WHERE `id` = ? AND `owner` = ?", id, player);
//            } catch (Exception var5) {
//                LOGGER.log(Level.WARNING, "removeSkin(\"" + id + "\", \"" + player + "\")", var5);
//            }
//        }
//
//    }
//
//    public static Property getOrCreateSkinForPlayer(String name, String id) {
//        Property prop = getPlayerSkin(id);
//        if (prop != null) {
//            return prop;
//        } else {
//            try {
//                String uid = Mojang.getUUID(name);
//                if (uid == null) {
//                    throw new InvalidMojangException("");
//                }
//
//                String uprop = Mojang.getSkinProperty(uid);
//                if (uprop == null) {
//                    throw new InvalidMojangException("");
//                }
//
//                prop = new Property(uprop.split(" : ")[0], uprop.split(" : ")[1], uprop.split(" : ")[2]);
//            } catch (InvalidMojangException var5) {
//                prop = new Property("textures", Language.options$default_skin$value, Language.options$default_skin$signature);
//            }
//
//            Database.getInstance().execute("INSERT INTO `playerskins` VALUES (?, ?, ?, ?, ?, ?, ?)", 0, id, id.replace("-", ""), name, prop.getValue(), prop.getSignature(), System.currentTimeMillis());
//            return prop;
//        }
//    }
//
//    public static Property getPlayerSkin(String id) {
//        int slot = getSlot(id);
//        CachedRowSet rs = Database.getInstance().query("SELECT * FROM `playerskins` WHERE `owner` = ? AND `id` = ?", id, slot);
//        if (rs != null) {
//            try {
//                String value = rs.getString("value");
//                String signature = rs.getString("signature");
//                return createProperty("textures", value, signature);
//            } catch (SQLException var5) {
//                var5.printStackTrace();
//                LOGGER.log(Level.WARNING, "getPlayerSkin(\"" + id + "\"): ", var5);
//            }
//        } else if (slot != 0) {
//            setSlot(id, 0);
//            return getPlayerSkin(id);
//        }
//
//        return null;
//    }
//
//    public static Integer hasSkin(String id, String skinName) {
//        Integer slot = null;
//        CachedRowSet rs = Database.getInstance().query("SELECT * FROM `playerskins` WHERE `owner` = ? AND `name` = ?", id, skinName);
//        if (rs != null) {
//            try {
//                slot = rs.getInt("id");
//            } catch (SQLException var5) {
//                LOGGER.log(Level.WARNING, "hasSkin(\"" + id + "\", \"" + skinName + "\"): ", var5);
//            }
//        }
//
//        return slot;
//    }
//
//    public static int getSlot(String id) {
//        CachedRowSet rs = Database.getInstance().query("SELECT * FROM `playerskin` WHERE `id` = ?", id);
//        if (rs != null) {
//            try {
//                return rs.getInt("slot");
//            } catch (SQLException var3) {
//                LOGGER.log(Level.WARNING, "getSlot(\"" + id + "\"): ", var3);
//            }
//        } else {
//            Database.getInstance().execute("INSERT INTO `playerskin` VALUES (?, ?)", id, 0);
//        }
//
//        return 0;
//    }
//
//    public static int getCurrentId(String id) {
//        int limit = Group.getLimit(ProxyServer.getInstance().getPlayer(UUID.fromString(id))) + 1;
//
//        for(int i = 1; i < limit; ++i) {
//            if (Database.getInstance().query("SELECT * FROM `playerskins` WHERE `owner` = ? AND `id` = ?", id.toString(), i) == null) {
//                return i;
//            }
//        }
//
//        return limit;
//    }
//
//    static {
//        LOGGER = Core.LOGGER.getModule("SKIN_METHODS");
//    }
//}