package net.vexmos.spigot.commands;

import net.vexmos.spigot.Services;
import net.vexmos.spigot.api.SpigotConfig;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class EventCommand extends Commands implements Listener {

    private final SpigotConfig eventosConfig;

    public EventCommand() {
        super("evento", "event");
        this.eventosConfig = new SpigotConfig("eventos.yml");

        // Ensure the file is saved when the command is initialized
        eventosConfig.saveDefaultConfig();

        // Register the event listener
        Bukkit.getPluginManager().registerEvents(this, Services.get());
    }

    private static final List<String> REQUIRED_PERMISSIONS = Arrays.asList(
            "group.diretor", "group.dev", "group.admin"
    );

    private boolean hasRequiredPermission(CommandSender sender) {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (sender.hasPermission(permission)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void perform(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cApenas Players podem usar este comando.");
            return;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            openEventGUI(player); // Open GUI for everyone when just "/evento" is typed
            return;
        }

        if (!hasRequiredPermission(player)) {
            player.sendMessage("§cVocê não tem permissão para isto.");
            return;
        }

        switch (args[0].toLowerCase()) {
            case "criar":
            case "create":
                createEvent(player, args);
                break;

            case "deletar":
            case "delete":
                deleteEvent(player, args);
                break;

            case "lista":
            case "list":
                listEvents(player);
                break;

            case "?":
            case "ajuda":
            case "help":
                sendHelpMessage(player);
                break;

            case "start":
                startEvent(player, args);
                break;

            case "parar":
            case "stop":
                stopEvent(player, args);
                break;

            default:
                openEventGUI(player);
                break;
        }
    }

    private void sendHelpMessage(Player player) {
        player.sendMessage("§eComando de eventos:");
        player.sendMessage("§6/evento criar <evento> <servidor> <descrição> §b- Cria um evento no seguinte formato.");
        player.sendMessage("§6/evento deletar <evento> - §bDeleta um evento existente.");
        player.sendMessage("§6/evento lista §b- Mostra os eventos existentes.");
        player.sendMessage("§6/evento ajuda §b- Mostra as funções de evento.");
    }

    private void createEvent(Player player, String[] args) {
        if (args.length < 4) {
            player.sendMessage("§cUso incorreto. Tente: /evento criar <evento> <servidor> <descrição>");
            return;
        }

        String eventName = args[1];
        String serverName = args[2];
        StringBuilder description = new StringBuilder();
        for (int i = 3; i < args.length; i++) {
            description.append(args[i]).append(" ");
        }

        FileConfiguration config = eventosConfig.getConfig();
        if (config.contains("events." + eventName)) {
            player.sendMessage("§cO evento " + eventName + " já existe.");
            return;
        }

        config.set("events." + eventName + ".server", serverName);
        config.set("events." + eventName + ".description", description.toString().trim());
        eventosConfig.saveConfig();

        player.sendMessage("§aO evento " + eventName + " foi criado com sucesso!");
    }

    private void stopEvent(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("§cUso incorreto. Tente: /evento stop <evento>");
            return;
        }

        String eventName = args[1];
        FileConfiguration config = eventosConfig.getConfig();

        // Check if the event exists
        if (!config.contains("events." + eventName)) {
            player.sendMessage("§cEsse evento não existe.");
            return;
        }

        // Retrieve the list of currently active events
        List<String> activeEvents = config.getStringList("current_event");
        if (activeEvents == null || !activeEvents.contains(eventName)) {
            player.sendMessage("§cEsse evento não está em andamento.");
            return;
        }

        // Remove the event from the list
        activeEvents.remove(eventName);
        config.set("current_event", activeEvents);
        eventosConfig.saveConfig();
        player.sendMessage("§aO evento " + eventName + " foi parado com sucesso!");
    }

    private void deleteEvent(Player player, String[] args) {

        if (args.length < 2) {
            player.sendMessage("§cUso incorreto. Tente: /evento deletar <evento>");
            return;
        }

        String eventName = args[1];
        FileConfiguration config = eventosConfig.getConfig();
        List<String> activeEvents = config.getStringList("current_event");
        activeEvents.remove(eventName);
        config.set("current_event", activeEvents);
        eventosConfig.saveConfig();

        if (!config.contains("events." + eventName)) {
            player.sendMessage("§cO evento " + eventName + " não existe.");
            return;
        }

        config.set("events." + eventName, null);
        eventosConfig.saveConfig();
        player.sendMessage("§aO evento " + eventName + " foi deletado com sucesso!");
    }

    private void listEvents(Player player) {
        FileConfiguration config = eventosConfig.getConfig();

        // Check if the "events" section exists
        if (!config.contains("events") || config.getConfigurationSection("events") == null) {
            player.sendMessage("§cNão há eventos existentes.");
            return;
        }

        Set<String> events = config.getConfigurationSection("events").getKeys(false);

        if (events == null || events.isEmpty()) {
            player.sendMessage("§cNão há eventos existentes.");
            return;
        }

        player.sendMessage("§eEventos existentes:");
        for (String eventName : events) {
            String description = config.getString("events." + eventName + ".description", "§cSem descrição.");
            player.sendMessage("§6" + eventName + ": §b" + description);
        }
    }

    private void startEvent(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("§cUso incorreto. Tente: /evento start <evento>");
            return;
        }

        String eventName = args[1];
        FileConfiguration config = eventosConfig.getConfig();

        if (!config.contains("events." + eventName)) {
            player.sendMessage("§cO evento " + eventName + " não existe.");
            return;
        }

        // Retrieve the list of currently active events, or initialize it if not present
        List<String> activeEvents = config.getStringList("current_event");
        if (activeEvents == null) {
            activeEvents = new ArrayList<>();
        }

        // Add the new event to the list if it's not already active
        if (!activeEvents.contains(eventName)) {
            activeEvents.add(eventName);
            config.set("current_event", activeEvents);
            eventosConfig.saveConfig();
            player.sendMessage("§aO evento " + eventName + " foi iniciado!");
        } else {
            player.sendMessage("§cO evento " + eventName + " já está ativo.");
        }
    }


    private void openEventGUI(Player player) {
        FileConfiguration config = eventosConfig.getConfig();
        List<String> activeEvents = config.getStringList("current_event");

        Inventory gui = Bukkit.createInventory(null, 5 * 9, "Eventos");

        if (!activeEvents.isEmpty()) {
            int baseSlot = 10; // Starting slot (arbitrary, adjust as needed)
            for (int i = 0; i < activeEvents.size(); i++) {
                String eventName = activeEvents.get(i);
                String serverName = config.getString("events." + eventName + ".server");
                String description = config.getString("events." + eventName + ".description");

                ItemStack eventItem = new ItemStack(Material.NETHER_STAR);
                ItemMeta meta = eventItem.getItemMeta();
                meta.setDisplayName("§a" + eventName);
                meta.setLore(Arrays.asList("§e" + description));
                eventItem.setItemMeta(meta);

                int slot = baseSlot + (i * 3); // Add 3 slots space for each event
                gui.setItem(slot, eventItem); // Place the event item in the GUI
            }
        } else {
            ItemStack noEventItem = new ItemStack(Material.BARRIER);
            ItemMeta meta = noEventItem.getItemMeta();
            meta.setDisplayName("§cNão há eventos ocorrendo");
            meta.setLore(Arrays.asList("§8Volte mais tarde."));
            noEventItem.setItemMeta(meta);

            gui.setItem(22, noEventItem); // Set in the middle of the GUI
        }

        player.openInventory(gui);
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getTitle().equals("Eventos")) {
            event.setCancelled(true); // Cancels the click event, making the GUI unclickable

            // Get the clicked item
            ItemStack clickedItem = event.getCurrentItem();

            // Check if the clicked item is an event item
            if (clickedItem != null && clickedItem.getType() == Material.NETHER_STAR) {
                // Get the event name from the item's display name
                String eventName = clickedItem.getItemMeta().getDisplayName().replace("§a", "");

                // Get the server name from the config
                FileConfiguration config = eventosConfig.getConfig();
                String serverName = config.getString("events." + eventName + ".server");
                ServerInfo target = ProxyServer.getInstance().getServerInfo(serverName);

                // Teleport the player to the event server
                ((Player) event.getWhoClicked()).sendMessage("§aVocê está sendo teleportado para o servidor do evento " + eventName + "!");
                // You need to use a BungeeCord API to connect to the server
                // For example, using BungeeAPI plugin:
                ProxiedPlayer p = (ProxiedPlayer) event.getWhoClicked();
                p.connect(target);
            }
        }
    }
}