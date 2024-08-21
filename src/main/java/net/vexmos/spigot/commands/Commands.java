package net.vexmos.spigot.commands;

import net.vexmos.database.spigot.ConnectSpigot;
import net.vexmos.spigot.listeners.PlayerUpdateListener;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;

public abstract class Commands extends Command {


    ConnectSpigot database = new ConnectSpigot();

    @Override
    public boolean execute(CommandSender sender, String cmdLabel, String[] args){
        if(sender instanceof Player){
            PlayerUpdateListener update = new PlayerUpdateListener(database);
            Player player = (Player) sender;
            update.updatePlayerGroup(player);
        }
        this.perform(sender, cmdLabel, args);
        return true;
    }


    public abstract void perform(CommandSender sender, String label, String[] args);

    protected Commands(String name, String... aliases) {
        super(name);
        // Register command with name and aliases
        try {
            SimpleCommandMap scm = (SimpleCommandMap) Bukkit.getServer().getClass().getDeclaredMethod("getCommandMap").invoke(Bukkit.getServer());
            scm.register("VexmosNET", this); // Registers the main command

            // Register aliases
            if (aliases != null && aliases.length > 0) {
                for (String alias : aliases) {
                    scm.register(alias, "vexmosnet", this);
                }
            }
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }

    public static void setup(){
        new FlyCommand();
        new GameModeCommand();
        //new ImageCommand();
        new TpCommand();
        new ChatCommand();
        new PosCommand();
        new EventCommand();
        new SystemCommand();
        //new NPCCommand();
        new TagsCommand();
    }

}