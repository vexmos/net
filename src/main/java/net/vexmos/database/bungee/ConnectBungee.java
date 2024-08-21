    package net.vexmos.database.bungee;

    import net.vexmos.proxy.api.BungeeConfig;
    import net.vexmos.proxy.api.PermissionAPI;
    import net.md_5.bungee.api.ProxyServer;
    import net.md_5.bungee.api.connection.ProxiedPlayer;
    import net.vexmos.spigot.api.PermissionConfig;
    import org.bukkit.entity.Player;

    import java.sql.*;
    import java.util.Set;

    public class ConnectBungee {
        private Connection connection;
        private String host, port, database, username, password;

        public ConnectBungee() {
            BungeeConfig config = new BungeeConfig("database.yml");
            config.saveDefault();
            host = config.getConfig().getString("mysql.host");
            port = config.getConfig().getString("mysql.port");
            database = config.getConfig().getString("mysql.database");
            username = config.getConfig().getString("mysql.username");
            password = config.getConfig().getString("mysql.password");
            connect();
            createGroupsTable();  // Criar a tabela de grupos ao conectar.
            createCoinsTable();
            createCristalTable();
            createTellTable();
            createPunishmentsTable();

        }

        public void createPunishmentsTable() {
            String query = "CREATE TABLE IF NOT EXISTS punishments (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "player_name VARCHAR(16), " +
                    "player_uuid VARCHAR(36), " +
                    "punisher_name VARCHAR(16), " +
                    "punishment_type ENUM('BAN', 'MUTE'), " +
                    "reason VARCHAR(255), " +
                    "punishment_time TIMESTAMP, " +
                    "expiration_time TIMESTAMP NULL, " +
                    "is_permanent BOOLEAN DEFAULT FALSE);";
            try {
                prepareStatement(query).executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void insertPunishment(String playerName, String playerUuid, String punisherName, String type, String reason, Timestamp expiration, boolean isPermanent) {
            String query = "INSERT INTO punishments (player_name, player_uuid, punisher_name, punishment_type, reason, punishment_time, expiration_time, is_permanent) VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?, ?);";
            try {
                PreparedStatement statement = prepareStatement(query);
                statement.setString(1, playerName);
                statement.setString(2, playerUuid);
                statement.setString(3, punisherName);
                statement.setString(4, type);
                statement.setString(5, reason);
                statement.setTimestamp(6, expiration);
                statement.setBoolean(7, isPermanent);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public ResultSet getPunishment(String playerUuid) {
            String query = "SELECT * FROM punishments WHERE player_uuid = ? AND (is_permanent = TRUE OR expiration_time > CURRENT_TIMESTAMP) LIMIT 1;";
            try {
                PreparedStatement statement = prepareStatement(query);
                statement.setString(1, playerUuid);
                return statement.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        public void removePunishment(String playerUuid) {
            String query = "DELETE FROM punishments WHERE player_uuid = ?;";
            try {
                PreparedStatement statement = prepareStatement(query);
                statement.setString(1, playerUuid);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }



        public void createTellTable() {
            String query = "CREATE TABLE IF NOT EXISTS tell_status (" +
                    "player_name VARCHAR(16) PRIMARY KEY, " +
                    "tell_enabled BOOLEAN DEFAULT TRUE);";  // Padrão TRUE (Tell ativado).
            try {
                prepareStatement(query).executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }




        // Define o status do Tell (on/off) para o jogador.
        public void setTellStatus(String playerName, boolean isEnabled) {
            String query = "INSERT INTO tell_status (player_name, tell_enabled) VALUES (?, ?) " +
                    "ON DUPLICATE KEY UPDATE tell_enabled=?;";
            try {
                PreparedStatement statement = prepareStatement(query);
                statement.setString(1, playerName);
                statement.setBoolean(2, isEnabled);
                statement.setBoolean(3, isEnabled);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }



        // Verifica se o Tell está ativado ou desativado para o jogador.
        public boolean isTellEnabled(String playerName) {
            String query = "SELECT tell_enabled FROM tell_status WHERE player_name=?";
            try {
                PreparedStatement statement = prepareStatement(query);
                statement.setString(1, playerName);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getBoolean("tell_enabled");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return true;  // Padrão é TRUE (Tell ativado).
        }




        public Connection getConnection() {
            if (!isConnected()) {
                connect();
            }
            return connection;
        }





        public void createCoinsTable() {
            String query = "CREATE TABLE IF NOT EXISTS coins (" +
                    "player_name VARCHAR(16) PRIMARY KEY, " +
                    "coins INT);";
            try {
                prepareStatement(query).executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void createCristalTable() {
            String query = "CREATE TABLE IF NOT EXISTS cristais (" +
                    "player_name VARCHAR(16) PRIMARY KEY, " +
                    "cristais INT);";
            try {
                prepareStatement(query).executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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

        public void createGroupsTable() {
            String query = "CREATE TABLE IF NOT EXISTS groups (" +
                    "player_name VARCHAR(16) PRIMARY KEY, " +
                    "group_name VARCHAR(16));";
            try {
                prepareStatement(query).executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void setPlayerGroup(String playerName, String group) {
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerName);
            if (player != null) {

                // Remove todas as permissões atuais do jogador
                Set<String> currentPermissions = PermissionAPI.getInstance().getPermissions(player);
                for (String permission : currentPermissions) {
                    player.setPermission(permission, false);
                }

                // Atualiza o grupo no banco de dados
                String query = "INSERT INTO groups (player_name, group_name) VALUES (?, ?) " +
                        "ON DUPLICATE KEY UPDATE group_name=?;";
                try {
                    PreparedStatement statement = prepareStatement(query);
                    statement.setString(1, playerName);
                    statement.setString(2, group);
                    statement.setString(3, group);
                    statement.executeUpdate();

                    // Adiciona as novas permissões do grupo
                    Set<String> newPermissions = PermissionAPI.getInstance().getPermissions(player);
                    for (String permission : newPermissions) {
                        player.setPermission(permission, true);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Jogador não encontrado online: " + playerName);
            }
        }

        public String getPlayerGroup(String playerName) {
            String query = "SELECT group_name FROM groups WHERE player_name=?";
            try {
                PreparedStatement statement = prepareStatement(query);
                statement.setString(1, playerName);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getString("group_name");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return "membro";  // Retorna "membro" como padrão se não encontrar o grupo.
        }
    }