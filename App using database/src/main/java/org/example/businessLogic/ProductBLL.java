package org.example.businessLogic;

import org.example.dataAccessLayer.ProductDAO;
import org.example.model.Product;

import java.util.List;

public class ProductBLL {
    private ProductDAO productDAO = new ProductDAO();
    private String message;

    /**
     *
     * @return a useful string to inform the user of the result of the operation
     */
    public String getMessage() {return message;}

    public void insert(Product product) {
        message = Validator.check(product);

        if(productDAO.findById(product.getID()) != null) {
            List<Product> list = productDAO.findById(product.getID());
            System.out.println("Exista deja");
            boolean thisIdAlreadyExists = false;
            for(Product p : list)
                thisIdAlreadyExists = true;
            if(thisIdAlreadyExists) {
                message = "Product with this id already exists";
                return;
            }
        }

        if(message.equals("Ok"))
            productDAO.insert(product);
    }

    public List<Product> view() {
        return productDAO.viewTable();
    }

    public List<Product> findById(int id) {
        return productDAO.findById(id);
    }

    public List<Product> findByName(String name) {return productDAO.findByName(name);}

    /**
     * Apart from sending a query to the database, when deleting a product (because of the foreign key constrain)
     * all the orders with the id of this client must be deleted as well
     * @param name the name of the product we want to delete from the database.
     */
    public void delete(String name) {
        int id;
        try {
            id = productDAO.findByName(name).get(0).getID();
            new OrderBLL().deleteByProductID(id);
            productDAO.delete(name);
        }
        catch (Exception e) {
            message = "No such product exists";
        }
        message = "Deleted";
    }

    public void update(String name, String field, String newValue) {
        productDAO.update(name, field, newValue);
    }

    public void updateStock(String name, int newValue) {
        productDAO.updateStock(name, newValue);
    }
}
