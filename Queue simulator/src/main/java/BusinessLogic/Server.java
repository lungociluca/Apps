package BusinessLogic;

import DataModels.Task;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable{
    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;
    private AtomicBoolean run;
    private AtomicInteger clients;

    public Server() {
        tasks = new ArrayBlockingQueue<Task>(50);
        waitingPeriod = new AtomicInteger(0);
        run = new AtomicBoolean(true);
        clients = new AtomicInteger(0);
    }

    public int getWaitingPeriod() {
        return waitingPeriod.get();
    }

    public void addTask(Task newTask) throws InterruptedException {
        waitingPeriod.addAndGet(newTask.getProcessingTime());
        clients.getAndIncrement();
        tasks.put(newTask);
    }

    public void endRun() {
        run.set(false);
    }

    public int getClients() {
        return clients.get();
    }

    public void run() {
        while(run.get()) {
            Task nextTask = tasks.peek();
            try {
                if(nextTask != null) {
                    for(Task t : tasks)
                        if(t != nextTask)
                            t.incrementWaitingTime();

                    if(nextTask.getProcessingTime() == 1) {
                        System.out.println("Task "+nextTask.getID()+" finished");
                        Task finished =  tasks.take();
                        toString(finished);
                        clients.getAndDecrement();
                        waitingPeriod.getAndDecrement();
                        if(waitingPeriod.get() < 0)
                            System.out.println("WADDDDDDDDD");
                    }
                    else {
                        nextTask.decrementServiceTime();
                        waitingPeriod.getAndDecrement();
                    }
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Could not take task");
            }
        }
    }

    private static String toString(Task t) {
        String string = "("+t.getID()+", "+t.getArrivalTime()+", "+t.getProcessingTime()+")";
        System.out.println(string);
        return string;
    }

    public String getTasks() {
        String s = "";
        for(Task t : tasks) {
            s += Server.toString(t) + " ";
        }
        return s;
    }
}
