package org.example.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseManager {

    private static final String URL = "jdbc:mysql://localhost:3306/pharmacy_db";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void saveClient(String name, String dni) {
        String sql = "INSERT INTO Client (name, dni) VALUES (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {


            pstmt.setString(1, name);
            pstmt.setString(2, dni);


            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Cliente guardado exitosamente. Filas afectadas: " + rowsAffected);

        } catch (SQLException e) {
            System.out.println("Error al guardar el cliente: " + e.getMessage());
        }
    }
}
