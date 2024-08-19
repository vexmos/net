package net.vexmos.proxy.commands;

import net.vexmos.proxy.VexmosPROXY;
import net.vexmos.database.bungee.ConnectBungee;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class CristalSystem extends Commands implements Listener {

    private ConnectBungee connectBungee;

    public CristalSystem() {
        super("cristais");
        connectBungee = new ConnectBungee();
        VexmosPROXY.get().getProxy().getPluginManager().registerListener(VexmosPROXY.get(), this);
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
    public void perform(CommandSender sender, String[] args) {
        if (!hasRequiredPermission(sender)) {
            sender.sendMessage(new TextComponent("§cVocê não tem permissão para isso."));
            return;
        }

        if (args.length < 1) {
            if (sender instanceof ProxiedPlayer) {
                ProxiedPlayer player = (ProxiedPlayer) sender;
                int cristais = getCristais(player.getName());
                sender.sendMessage(new TextComponent("§aVocê tem " + cristais + " cristais."));
            } else {
                sender.sendMessage(new TextComponent("§cEste comando só pode ser executado por jogadores."));
            }
            return;
        }

        if (args[0].equalsIgnoreCase("set")) {
            if (args.length < 3) {
                sender.sendMessage(new TextComponent("§cUso: /cristais set <jogador> <quantidade>"));
                return;
            }
            String player = args[1];
            int amount = Integer.parseInt(args[2]);
            setCristais(player, amount);
            sender.sendMessage(new TextComponent("§aCristais do jogador " + player + " foram setados para " + amount + "."));
        } else if (args[0].equalsIgnoreCase("add")) {
            if (args.length < 3) {
                sender.sendMessage(new TextComponent("§cUso: /cristais add <jogador> <quantidade>"));
                return;
            }
            String player = args[1];
            int amount = Integer.parseInt(args[2]);
            addCristais(player, amount);
            sender.sendMessage(new TextComponent("§a" + amount + " cristais foram adicionados ao jogador " + player + "."));
        } else if (args[0].equalsIgnoreCase("remove")) {
            if (args.length < 3) {
                sender.sendMessage(new TextComponent("§cUso: /cristais remove <jogador> <quantidade>"));
                return;
            }
            String player = args[1];
            int amount = Integer.parseInt(args[2]);
            removeCristais(player, amount);
            sender.sendMessage(new TextComponent("§a" + amount + " cristais foram removidos do jogador " + player + "."));
        } else {
            sender.sendMessage(new TextComponent("§cUso: /cristais [set|add|remove] <jogador> <quantidade>"));
        }
    }

    public int getCristais(String player) {
        String query = "SELECT cristais FROM cristais WHERE player_name=?";
        try {
            PreparedStatement statement = connectBungee.prepareStatement(query);
            statement.setString(1, player);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("cristais");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }



    private void setCristais(String player, int amount) {
        String query = "INSERT INTO cristais (player_name, cristais) VALUES (?, ?) ON DUPLICATE KEY UPDATE cristais=?;";
        try {
            PreparedStatement statement = connectBungee.prepareStatement(query);
            statement.setString(1, player);
            statement.setInt(2, amount);
            statement.setInt(3, amount);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addCristais(String player, int amount) {
        int currentCristais = getCristais(player);
        setCristais(player, currentCristais + amount);
    }

    public void removeCristais(String player, int amount) {
        int currentCristais = getCristais(player);
        if (currentCristais >= amount) {
            setCristais(player, currentCristais - amount);
        }
    }
}