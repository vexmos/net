package net.vexmos.proxy.database;

import java.sql.*;

public class ProxyConnection {

    public static String host = Variables.host;
    public static String name = Variables.name;
    public static String username = Variables.username;
    public static String password = Variables.password;
    public static int port = Variables.port;

    public ProxyConnection(){
        setupTables();
    }

    public void setupTables(){
        createTable("languages");
    }

    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + name + username + password);
    }

    public static void createTable(String tableName) {
        String sql = "CREATE TABLE " + tableName + " (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255), age INT)";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Inserir dados
    public static void insertData(String tableName, String name, int age) {
        String sql = "INSERT INTO " + tableName + " (name, age) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.executeUpdate();
            System.out.println("Data inserted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Ler dados
    public static void readData(String tableName) {
        String sql = "SELECT * FROM " + tableName;
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                System.out.println("ID: " + id + ", Name: " + name + ", Age: " + age);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Deletar dados
    public static void deleteData(String tableName, int id) {
        String sql = "DELETE FROM " + tableName + " WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Data deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Deletar tabela
    public static void dropTable(String tableName) {
        String sql = "DROP TABLE " + tableName;
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table dropped successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
