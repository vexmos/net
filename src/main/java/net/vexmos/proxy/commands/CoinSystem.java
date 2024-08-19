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

public class CoinSystem extends Commands implements Listener {

    private ConnectBungee connectBungee;

    public CoinSystem() {
        super("coins", "moedas");
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
                int coins = getCoins(player.getName());
                sender.sendMessage(new TextComponent("§aVocê tem " + coins + " coins."));
            } else {
                sender.sendMessage(new TextComponent("§cEste comando só pode ser executado por jogadores."));
            }
            return;
        }

        if (args[0].equalsIgnoreCase("set")) {
            if (args.length < 3) {
                sender.sendMessage(new TextComponent("§cUso: /coins set <jogador> <quantidade>"));
                return;
            }
            String player = args[1];
            int amount = Integer.parseInt(args[2]);
            setCoins(player, amount);
            sender.sendMessage(new TextComponent("§aCoins do jogador " + player + " foram setados para " + amount + "."));
        } else if (args[0].equalsIgnoreCase("add")) {
            if (args.length < 3) {
                sender.sendMessage(new TextComponent("§cUso: /coins add <jogador> <quantidade>"));
                return;
            }
            String player = args[1];
            int amount = Integer.parseInt(args[2]);
            addCoins(player, amount);
            sender.sendMessage(new TextComponent("§a" + amount + " coins foram adicionados ao jogador " + player + "."));
        } else if (args[0].equalsIgnoreCase("remove")) {
            if (args.length < 3) {
                sender.sendMessage(new TextComponent("§cUso: /coins remove <jogador> <quantidade>"));
                return;
            }
            String player = args[1];
            int amount = Integer.parseInt(args[2]);
            removeCoins(player, amount);
            sender.sendMessage(new TextComponent("§a" + amount + " coins foram removidos do jogador " + player + "."));
        } else {
            sender.sendMessage(new TextComponent("§cUso: /coins [set|add|remove] <jogador> <quantidade>"));
        }
    }

    public int getCoins(String player) {
        String query = "SELECT coins FROM coins WHERE player_name=?";
        try {
            PreparedStatement statement = connectBungee.prepareStatement(query);
            statement.setString(1, player);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("coins");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }



    private void setCoins(String player, int amount) {
        String query = "INSERT INTO coins (player_name, coins) VALUES (?, ?) ON DUPLICATE KEY UPDATE coins=?;";
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

    private void addCoins(String player, int amount) {
        int currentCoins = getCoins(player);
        setCoins(player, currentCoins + amount);
    }

    public void removeCoins(String player, int amount) {
        int currentCoins = getCoins(player);
        if (currentCoins >= amount) {
            setCoins(player, currentCoins - amount);
        }
    }
}