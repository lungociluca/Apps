package org.example.dataAccessLayer;

import org.example.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

public class ProductDAO extends AbstractDAO<Product> {
    public ProductDAO() {
        super(Product.class);
    }

    /**
     * When an order is placed, the number of products available has to be updated
     * @param name the name of the product placed for the order
     * @param newValue the value it has to be updated to
     */
    public void updateStock(String name, int newValue) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = "UPDATE schooldb.Product SET stock = " + newValue + " WHERE name = '" + name + "'";
        System.out.println(query);

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            LOGGER.log(Level.WARNING, "ProductDAO " + " DAO:update " + e.getMessage());
        }
        finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }
}
