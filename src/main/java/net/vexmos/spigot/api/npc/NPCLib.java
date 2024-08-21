//
//package net.vexmos.spigot.api.npc;
//
//import net.vexmos.spigot.api.npc.api.utilities.Logger;
//import net.vexmos.spigot.api.npc.listeners.ChunkListener;
//import net.vexmos.spigot.api.npc.listeners.PeriodicMoveListener;
//import net.vexmos.spigot.api.npc.listeners.PlayerListener;
//import net.vexmos.spigot.api.npc.listeners.PlayerMoveEventListener;
//import net.vexmos.spigot.api.npc.metrics.NPCLibMetrics;
//import net.vexmos.spigot.listeners.server.PacketListener;
//import org.bukkit.plugin.PluginManager;
//import org.bukkit.plugin.java.JavaPlugin;
//
//import java.util.List;
//
//public final class NPCLib {
//
//    private final JavaPlugin plugin;
//    private final Logger logger;
//    private final Class<?> npcClass;
//
//    private double autoHideDistance = 50.0;
//
//    private NPCLib(JavaPlugin plugin, MovementHandling moveHandling) {
//        this.plugin = plugin;
//        this.logger = new Logger("VexmosNET");
//
//        String versionName = plugin.getServer().getClass().getPackage().getName().split("\\.")[3];
//
//        Class<?> npcClass = null;

//        this.npcClass = npcClass;

//
//        PluginManager pluginManager = plugin.getServer().getPluginManager();
//
//        pluginManager.registerEvents(new PlayerListener(this, );
//        pluginManager.registerEvents(new ChunkListener(this, plugin);
//
//        if (moveHandling.usePme) {
//            pluginManager.registerEvents(new PlayerMoveEventListener(), plugin);
//        } else {
//            pluginManager.registerEvents(new PeriodicMoveListener(this, moveHandling.updateInterval), plugin);
//        }
//
//        // Boot the according packet listener.
//        new PacketListener().start(this);
//
//        // Start the bStats metrics system and disable the silly relocate check.
//        System.setProperty("bstats.relocatecheck", "false");
//        new NPCLibMetrics(this);
//
//        logger.info("Enabled for Minecraft " + versionName);
//    }
//
//    public NPCLib(JavaPlugin plugin) {
//        this(plugin, MovementHandling.playerMoveEvent());
//    }
//
//    public NPCLib(JavaPlugin plugin, NPCLibOptions options) {
//        this(plugin, options.moveHandling);
//    }
//
//    /**
//     * @return The JavaPlugin instance.
//     */
//    public JavaPlugin getPlugin() {
//        return plugin;
//    }
//
//    /**
//     * Set a new value for the auto-hide distance.
//     * A recommended value (and default) is 50 blocks.
//     *
//     * @param autoHideDistance The new value.
//     */
//    public void setAutoHideDistance(double autoHideDistance) {
//        this.autoHideDistance = autoHideDistance;
//    }
//
//    /**
//     * @return The auto-hide distance.
//     */
//    public double getAutoHideDistance() {
//        return autoHideDistance;
//    }
//
//    /**
//     * @return The logger NPCLib uses.
//     */
//    public Logger getLogger() {
//        return logger;
//    }
//
//    /**
//     * Create a new non-player character (NPC).
//     *
//     * @param text The text you want to sendShowPackets above the NPC (null = no text).
//     * @return The NPC object you may use to sendShowPackets it to players.
//     */
//    public NPC createNPC(List<String> text) {
//        try {
//            return (NPC) npcClass.getConstructors()[0].newInstance(this, text);
//        } catch (Exception exception) {
//            logger.warning("Failed to create NPC. Please report the following stacktrace message", exception);
//        }
//
//        return null;
//    }
//
//    /**
//     * Create a new non-player character (NPC).
//     *
//     * @return The NPC object you may use to sendShowPackets it to players.
//     */
//    public NPC createNPC() {
//        return createNPC(null);
//    }
//}