package DataModels;

import BusinessLogic.ConcreteStrategyQueue;
import BusinessLogic.ConcreteStrategyTime;
import BusinessLogic.Server;
import BusinessLogic.Strategy;
import DataModels.Task;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private List<Server> servers;
    private int maxNoServers;
    private int maxTasksPerServer;
    private Strategy strategy;
    private int peakHour, maxNrOfClients;

    public Scheduler(int maxNoServers, int maxTasksPerServer) {
        servers = new ArrayList<>();
        strategy = new ConcreteStrategyTime();
        peakHour = maxNrOfClients = 0;
        for(int i = 0; i < maxNoServers; i++) {
            servers.add(new Server());
            Thread thread = new Thread(servers.get(i));
            thread.start();
        }
    }

    public void changeStrategy() {
        if(strategy.getClass().getName().equals("ConcreteStrategyTime"))
            strategy = new ConcreteStrategyQueue();
        else
            strategy = new ConcreteStrategyTime();
    }

    public void dispatchTask(Task t) {
        try {
            strategy.addTask(servers, t);
        } catch (Exception e) {
            System.out.println("Error in 'addTask' method from Strategy");
        }

    }

    public void endServers() {
        for(Server s : servers)
            s.endRun();
    }

    public void forPeakHour(int time) {
        int sumClients = 0;
        for(Server s : servers) {
            sumClients += s.getClients();
        }
        if(sumClients > maxNrOfClients) {
            maxNrOfClients = sumClients;
            peakHour = time;
        }
    }

    public int getPeakHour() {
        return peakHour;
    }

    public String toString() {
        String s = "";
        int i = 1;
        for(Server server : servers) {
            s += "Queue " + i + ": ";
            s += server.getTasks() + ";\n";
            i++;
        }
        return s;
    }
}
