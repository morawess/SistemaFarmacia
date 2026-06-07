package org.example.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
/* DATABASE
--mySql--
Host:
Port: 3306
User: root
Password: 1234
Schema: pharmacy_db
URL: "jdbc:mysql://localhost:3306/pharmacy_db"
--cleverCloud--
Host / Server: bchfxdzarqvf7eul6zrv-mysql.services.clever-cloud.com
Port: 3306
User: uhbkv3lake9esy6q
Password: 25rKZQkn5gB48l8425IO
Database: bchfxdzarqvf7eul6zrv
URI: mysql://uhbkv3lake9esy6q:25rKZQkn5gB48l8425IO@bchfxdzarqvf7eul6zrv-mysql.services.clever-cloud.com:3306/bchfxdzarqvf7eul6zrv
 */

public class DatabaseManager {

    private static final String URL = "jdbc:mysql://bchfxdzarqvf7eul6zrv-mysql.services.clever-cloud.com:3306/bchfxdzarqvf7eul6zrv";
    private static final String USER = "uhbkv3lake9esy6q";
    private static final String PASSWORD = "25rKZQkn5gB48l8425IO";

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
