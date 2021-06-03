package org.example.dataAccessLayer;

import org.example.dataAccessLayer.AbstractDAO;
import org.example.model.Client;

public class ClientDAO extends AbstractDAO<Client> {

    public ClientDAO() {
        super(Client.class);
    }
}
