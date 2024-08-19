package net.vexmos.spigot.commands;

import net.vexmos.spigot.api.ActionBarAPI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class FlyCommand extends Commands {

    public FlyCommand(){
        super("fly", "voar");
    }

    private static final List<String> REQUIRED_PERMISSIONS = Arrays.asList(
            "group.dev", "group.diretor", "group.mod", "group.admin", "group.suporte", "grupo.construtor", "grupo.emerald", "grupo.diamond", "grupo.gold"
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
        if (!(sender instanceof Player)){
            sender.sendMessage("§cApenas Players podem usar este comando.");
            return;
        }

        assert sender instanceof Player;
        Player player = (Player) sender;

        if (!hasRequiredPermission(player)){
            sender.sendMessage("§cVocê não tem permissão para isto.");
            return;
        }

        if (!player.getAllowFlight()){
            player.setAllowFlight(true);
            ActionBarAPI.sendActionBar(player, "§aModo de Vôo ativado.");
        } else {
            player.setAllowFlight(false);
            ActionBarAPI.sendActionBar(player, "§cModo de Vôo desativado.");
        }

    }
}