package net.vexmos.spigot.commands;

import net.vexmos.database.spigot.ConnectSpigot;
import net.vexmos.spigot.listeners.PlayerUpdateListener;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public abstract class Commands extends Command {


    private PlayerUpdateListener playerUpdateListener;
    private static ConnectSpigot database;

    @Override
    public boolean execute(CommandSender sender, String cmdLabel, String[] args){
        this.perform(sender, cmdLabel, args);
        return true;
    }

    protected void updatePlayerGroup(Player player) {
        playerUpdateListener.updatePlayerGroup(player);
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