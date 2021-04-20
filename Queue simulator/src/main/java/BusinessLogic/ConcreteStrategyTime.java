package BusinessLogic;

import DataModels.Task;

import java.util.List;

public class ConcreteStrategyTime implements Strategy {
    @Override
    public void addTask(List<Server> servers, Task t) throws InterruptedException {
        int minWaitingTime = 999999, index = 0, minWaitingTimeIndex = 0;

        for(Server s : servers) {
            if(s.getWaitingPeriod() < minWaitingTime) {
                minWaitingTime = s.getWaitingPeriod();
                minWaitingTimeIndex = index;
            }
            index++;
        }

        Server toAdd = servers.get(minWaitingTimeIndex);
        try {
            toAdd.addTask(t);
        }catch (Exception e) {
            System.out.println("Could not add task, 'addTask()' in Server failed");
        }

    }
}
