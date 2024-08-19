package net.vexmos.spigot.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

public class SystemCommand extends Commands implements Listener {

    public SystemCommand(){
        super("system", "sistema", "vexmos", "vexmossystem", "vexmossistema", "vexmosplugin");
    }

    @Override
    public void perform(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage("§cApenas Players podem usar este comando.");
            return;
        }

        Player player = (Player) sender;

        Inventory inventory = Bukkit.createInventory(null, 3 * 9, "Vexmos");

        ItemStack vexmosHeader = new ItemStack(Material.PAPER);
        ItemMeta vexmosHeaderMeta = vexmosHeader.getItemMeta();
        vexmosHeaderMeta.setDisplayName("§6Vexmos 2025 ©");
        vexmosHeaderMeta.setLore(Arrays.asList("§eInício: §b07/08/2024", "§cTempo de desenvolvimento: §e" + getDevelopmentTime()));
        vexmosHeader.setItemMeta(vexmosHeaderMeta);
        inventory.setItem(4, vexmosHeader);

        // Usar o Material.SKULL_ITEM com o tipo 3 (Dúvida: Em versões mais recentes, é Material.PLAYER_HEAD ou SKULL)
        ItemStack thixgooHead = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta thixgooHeadMeta = (SkullMeta) thixgooHead.getItemMeta();
        thixgooHeadMeta.setOwner("thixgoo");
        thixgooHeadMeta.setDisplayName("§cSpirit");
        thixgooHeadMeta.setLore(Arrays.asList("§aFundador do Vexmos Inc.", "§3Desenvolvedor."));
        thixgooHead.setItemMeta(thixgooHeadMeta);
        inventory.setItem(12, thixgooHead);

        ItemStack nevesbr6Head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta nevesbr6HeadMeta = (SkullMeta) nevesbr6Head.getItemMeta();
        nevesbr6HeadMeta.setOwner("nevesbr6");
        nevesbr6HeadMeta.setDisplayName("§cNeves");
        nevesbr6HeadMeta.setLore(Arrays.asList("§aFundador do Vexmos Inc.", "§3Desenvolvedor."));
        nevesbr6Head.setItemMeta(nevesbr6HeadMeta);
        inventory.setItem(14, nevesbr6Head);

        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().equals("Vexmos")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getInventory().equals("Vexmos")) {
            event.setCancelled(true);
        }
    }




    private String getDevelopmentTime() {
        Instant startDate = LocalDate.of(2024, 8, 7).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        long seconds = ChronoUnit.SECONDS.between(startDate, Instant.now());
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        long months = days / 30;

        return String.format("%d meses, %d dias, %d horas, %d minutos e %d segundos", months, days % 30, hours % 24, minutes % 60, seconds % 60);
    }
}
