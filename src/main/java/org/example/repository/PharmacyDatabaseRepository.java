package org.example.repository;

import org.example.model.*;

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
        String sql = "SELECT id, name, price, stock, product_type FROM Product WHERE id = ?";
        Product product = null;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String type = rs.getString("product_type");
                    int prodId = rs.getInt("id");
                    String name = rs.getString("name");
                    double price = rs.getDouble("price");
                    int stock = rs.getInt("stock");

                    if ("OTC".equalsIgnoreCase(type)) {
                        product = new OTCProduct(prodId, name, price, stock);
                    } else {
                        product = new PrescriptionProduct(prodId, name, price, stock);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar producto: " + e.getMessage());
        }

        return product;
    }

    @Override
    public void processSale(Sale sale) {
        String insertSaleSql = "INSERT INTO Sale (client_id, total) VALUES (?, ?)";
        String insertDetailSql = "INSERT INTO SaleDetail (sale_id, product_id, quantity, unit_price, subtotal, discount_amount, final_total) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String updateStockSql = "UPDATE Product SET stock = stock - ? WHERE id = ?";

        Connection conn = null;

        try {
            conn = DatabaseManager.getConnection();
            conn.setAutoCommit(false);

            int generatedSaleId = 0;

            try (PreparedStatement pstmtSale = conn.prepareStatement(insertSaleSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                if (sale.getClient() != null) {
                    pstmtSale.setInt(1, sale.getClient().getId());
                } else {
                    pstmtSale.setNull(1, java.sql.Types.INTEGER); //cliente no registrado
                }
                pstmtSale.setDouble(2, sale.getTotal());
                pstmtSale.executeUpdate();

                try (ResultSet generatedKeys = pstmtSale.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedSaleId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("No se pudo obtener el ID de la Venta.");
                    }
                }
            }

            try (PreparedStatement pstmtDetail = conn.prepareStatement(insertDetailSql);
                 PreparedStatement pstmtStock = conn.prepareStatement(updateStockSql)) {

                for (SaleDetail detail : sale.getDetails()) {
                    // Guardar el renglón
                    pstmtDetail.setInt(1, generatedSaleId);
                    pstmtDetail.setInt(2, detail.getProduct().getId());
                    pstmtDetail.setInt(3, detail.getQuantity());
                    pstmtDetail.setDouble(4, detail.getUnitPrice());
                    pstmtDetail.setDouble(5, detail.getSubtotal());
                    pstmtDetail.setDouble(6, detail.getDiscountAmount());
                    pstmtDetail.setDouble(7, detail.getFinalTotal());
                    pstmtDetail.executeUpdate();

                    pstmtStock.setInt(1, detail.getQuantity());
                    pstmtStock.setInt(2, detail.getProduct().getId());
                    pstmtStock.executeUpdate();
                }
            }

            conn.commit();
            System.out.println("Venta registrada y stock actualizado con exito en la BD.");

        } catch (SQLException e) {
            System.out.println("Ocurrio un error en la venta. Deshaciendo cambios: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.out.println("Error critico al intentar hacer rollback: " + ex.getMessage());
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ex) {
                    System.out.println("Error al cerrar la conexión: " + ex.getMessage());
                }
            }
        }
    }
}