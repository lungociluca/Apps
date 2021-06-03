package org.example.dataAccessLayer;

import org.example.dataAccessLayer.AbstractDAO;
import org.example.model.Order;

public class OrderDAO extends AbstractDAO<Order> {
    public OrderDAO() {
        super(Order.class);
    }
}
