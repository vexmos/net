package net.vexmos.database.spigot;

import net.vexmos.spigot.api.SpigotConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectSpigot {

    private Connection connection;
    SpigotConfig config = new SpigotConfig("database.yml");
    private String host, port, database, username, password;

    public ConnectSpigot() {
        config.saveDefaultConfig();
        config.reloadConfig();
        host = config.getConfig().getString("mysql.host");
        port = config.getConfig().getString("mysql.port");
        database = config.getConfig().getString("mysql.database");
        username = config.getConfig().getString("mysql.username");
        password = config.getConfig().getString("mysql.password");
        connect();
        createTags();
    }

    public void connect() {
        if (!isConnected()) {
            try {
                connection = DriverManager.getConnection(
                        "jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
                System.out.println("MySQL conectado com sucesso!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public PreparedStatement prepareStatement(String query) throws SQLException {
        if (isConnected()) {
            return connection.prepareStatement(query);
        }
        return null;
    }

    public ResultSet query(String query) throws SQLException {
        if (isConnected()) {
            return prepareStatement(query).executeQuery();
        }
        return null;
    }

    public int update(String query) throws SQLException {
        if (isConnected()) {
            return prepareStatement(query).executeUpdate();
        }
        return 0;
    }

    public void createTags() {
        String query = "CREATE TABLE IF NOT EXISTS tags (" +
                "player_name VARCHAR(255) NOT NULL," +
                "tag VARCHAR(255) NOT NULL," +
                "PRIMARY KEY (player_name)" +
                ");";

        try {
            PreparedStatement statement = prepareStatement(query);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getPlayerGroup(String playerName) {
        String group = "membro";  // Valor padrão
        String query = "SELECT group_name FROM groups WHERE player_name = ?";

        try {
            PreparedStatement statement = prepareStatement(query);
            statement.setString(1, playerName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                group = resultSet.getString("group_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return group;
    }

    public void setPlayerGroup(String playerName, String group) {
        String query = "UPDATE groups SET group_name = ? WHERE player_name = ?";

        try {
            PreparedStatement statement = prepareStatement(query);
            statement.setString(1, group);
            statement.setString(2, playerName);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void setPlayerTag(String playerName, String tag) {
        String query = "SELECT * FROM tags WHERE player_name = ?";

        try {
            PreparedStatement statement = prepareStatement(query);
            statement.setString(1, playerName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Se o jogador já tem uma tag associada, atualize-a
                query = "UPDATE tags SET tag = ? WHERE player_name = ?";
                statement = prepareStatement(query);
                statement.setString(1, tag);
                statement.setString(2, playerName);
                statement.executeUpdate();
            } else {
                // Se o jogador não tem uma tag associada, crie um novo registro
                query = "INSERT INTO tags (player_name, tag) VALUES (?, ?)";
                statement = prepareStatement(query);
                statement.setString(1, playerName);
                statement.setString(2, tag);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getPlayerTag(String playerName) {
        String tag = null;
        String query = "SELECT tag FROM tags WHERE player_name = ?";

        try {
            PreparedStatement statement = prepareStatement(query);
            statement.setString(1, playerName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                tag = resultSet.getString("tag");
            } else {
                // Se o jogador não tem uma tag associada, retorne a tag padrão
                tag = "membro";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tag;
    }

}