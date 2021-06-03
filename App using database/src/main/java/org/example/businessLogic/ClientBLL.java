package org.example.businessLogic;

import org.example.dataAccessLayer.ClientDAO;
import org.example.model.Client;

import java.util.List;

public class ClientBLL {
    private ClientDAO clientDAO = new ClientDAO();
    private String message;

    /**
     *
     * @return a useful string to inform the user of the result of the operation
     */
    public String getMessage() {
        return message;
    }

    public void insert(Client client) {
        message = Validator.check(client);

        if(clientDAO.findById(client.getID()) != null) {
            List<Client> list = clientDAO.findById(client.getID());
            boolean thisIdAlreadyExists = false;
            for(Client c : list)
                thisIdAlreadyExists = true;
            if(thisIdAlreadyExists) {
                message = "Client with this id already exists";
                return;
            }
        }

        if(message.equals("Ok"))
            clientDAO.insert(client);
    }

    public List<Client> view() {
        return clientDAO.viewTable();
    }

    public List<Client> findById(int id) {
        return clientDAO.findById(id);
    }

    public List<Client> findByName(String name) {
        return clientDAO.findByName(name);
    }

    /**
     * Apart from sending a query to the database, when deleting a client (because of the foreign key constrain)
     * all the orders with the id of this client must be deleted as well
     * @param name the name of the client we want to delete from the database.
     */
    public void delete(String name) {
        int id;
        try {
            id = clientDAO.findByName(name).get(0).getID();
            new OrderBLL().deleteByClientID(id);
            clientDAO.delete(name);
        }
        catch (Exception e) {
            message = "No such client exists";
        }
        message = "Deleted";
    }

    public void update(String name, String field, String newValue) {
        clientDAO.update(name, field, newValue);
    }
}
