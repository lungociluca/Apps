package org.example.businessLogic;

import org.example.dataAccessLayer.ConnectionFactory;
import org.example.dataAccessLayer.OrderDAO;
import org.example.model.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OrderBLL {
    private OrderDAO orderDAO = new OrderDAO();
    private String message;

    /**
     *
     * @return a useful string to inform the user of the result of the operation
     */
    public String getMessage() {return message;}

    public void insert(Order order) {
        message = Validator.check(order);

        if(orderDAO.findById(order.getID()) != null) {
            List<Order> list = orderDAO.findById(order.getID());
            System.out.println("Exista deja");
            boolean thisIdAlreadyExists = false;
            for(Order o : list)
                thisIdAlreadyExists = true;
            if(thisIdAlreadyExists) {
                message = "Order with this id already exists";
                return;
            }
        }

        if(message.equals("Ok"))
            orderDAO.insert(order);
    }

    /**
     *
     * @param id deletes all the clients with this id
     */
    public void deleteByClientID(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = "DELETE FROM schooldb.Order WHERE client_ID = " + id;

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            System.out.println(query);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("cannot delete all orders, OrderBLL class");
        }
        finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

    /**
     *
     * @param id deletes all the products with this id
     */
    public void deleteByProductID(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "DELETE FROM schooldb.Order WHERE product_ID = " + id;

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            System.out.println(query);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("cannot delete all orders, OrderBLL class");
        }
        finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

}
