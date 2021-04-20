package BusinessLogic;

import DataModels.Task;

import java.util.List;

public class ConcreteStrategyQueue implements Strategy {
    @Override
    public void addTask(List<Server> servers, Task t) throws InterruptedException {
        int minClientsNumber = 999999, index = 0, minClientsNumberIndex = 0;

        for(Server s : servers) {
            if(s.getClients() < minClientsNumber) {
                minClientsNumber = s.getClients();
                minClientsNumberIndex = index;
            }
            index++;
        }

        Server toAdd = servers.get(minClientsNumberIndex);
        toAdd.addTask(t);
    }
}
