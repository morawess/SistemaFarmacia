package org.example.repository;

import org.example.model.Client;
import org.example.model.Product;
import org.example.model.Sale;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PharmacyDatabaseRepository implements ClientManageable, ProductConsultable, SaleProcessable {

    @Override
    public void saveClient(Client client) {
        String sql = "INSERT INTO Client (name, dni) VALUES (?, ?)";

        //esto hace que se cierre la conexión al terminar
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, client.getName());
            pstmt.setString(2, client.getDni());

            pstmt.executeUpdate();
            System.out.println("Cliente guardado en la base de datos con exito.");

        } catch (SQLException e) {
            System.out.println("Error al guardar el cliente: " + e.getMessage());
        }
    }

    @Override
    public Client getClientByDni(String dni) {
        String sql = "SELECT id, name, dni FROM Client WHERE dni = ?";
        Client client = null;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, dni);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    client = new Client(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("dni")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar cliente por DNI: " + e.getMessage());
        }

        return client;
    }

    @Override
    public Product getProductById(int id) {
        String sql = "SELECT id, name, price, stock FROM Product WHERE id = ?";
        Product product = null;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) { //si cuentra el producto rs.next es true
                    product = new Product(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            rs.getInt("stock")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar producto: " + e.getMessage());
        }

        return product; //si no lo encuentra devuelve null
    }

    @Override
    public void processSale(Sale sale, int productId, int quantity) {
        String insertSaleSql = "INSERT INTO Sale (client_id, total) VALUES (?, ?)";
        String updateStockSql = "UPDATE Product SET stock = stock - ? WHERE id = ?";

        Connection conn = null;

        try {
            conn = DatabaseManager.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement pstmtSale = conn.prepareStatement(insertSaleSql)) {
                pstmtSale.setInt(1, sale.getClientId());
                pstmtSale.setDouble(2, sale.getTotal());
                pstmtSale.executeUpdate();
            }

            try (PreparedStatement pstmtStock = conn.prepareStatement(updateStockSql)) {
                pstmtStock.setInt(1, quantity);
                pstmtStock.setInt(2, productId);
                pstmtStock.executeUpdate();
            }

            conn.commit();
            System.out.println("Venta registrada y stock actualizado con éxito en la base de datos");

        } catch (SQLException e) {
            //si algo falla abortamos misión y deshacemos los cambios (rollback)
            System.out.println("Ocurrió un error en la venta. Deshaciendo cambios: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.out.println("Error critico al intentar hacer rollback: " + ex.getMessage());
                }
            }
        } finally {
            //la cerramos al terminar
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); //se restaura el comportamiento default de mysql
                    conn.close();
                } catch (SQLException ex) {
                    System.out.println("Error al cerrar la conexión: " + ex.getMessage());
                }
            }
        }
    }
}