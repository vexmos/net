package net.vexmos.spigot.commands;

import net.vexmos.spigot.api.ActionBarAPI;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class GameModeCommand extends Commands {

    public GameModeCommand(){
        super("gm", "gamemode", "modo", "mode");
    }


    private static final List<String> REQUIRED_PERMISSIONS = Arrays.asList(
            "group.diretor", "group.dev", "group.mod", "group.admin"
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
        }

        assert sender instanceof Player;
        Player player = (Player) sender;

        if(!hasRequiredPermission(sender)) {
            sender.sendMessage("§cVocê não tem permissão para isto.");
            return;
        }

        if (args.length == 0) {
            player.sendMessage("§cUse: /gm (0 | 1 | 2 | 3)");
            return;
        }

        String mode = args[0];

        switch (mode) {
            case "0":
                player.setGameMode(GameMode.SURVIVAL);
                ActionBarAPI.sendActionBar(player, "§aModo de jogo alterado para §bSobrevivência");
                break;
            case "1":
                player.setGameMode(GameMode.CREATIVE);
                ActionBarAPI.sendActionBar(player, "§aModo de jogo alterado para §bCriativo");
                break;
            case "2":
                player.setGameMode(GameMode.ADVENTURE);
                ActionBarAPI.sendActionBar(player, "§aModo de jogo alterado para §bAventura");
                break;
            case "3":
                player.setGameMode(GameMode.SPECTATOR);
                ActionBarAPI.sendActionBar(player, "§aModo de jogo alterado para §bEspectador");
                break;
            default:
                player.sendMessage("§cUse: /gm (0 | 1 | 2 | 3)");
                break;
        }
        return;
    }
}
