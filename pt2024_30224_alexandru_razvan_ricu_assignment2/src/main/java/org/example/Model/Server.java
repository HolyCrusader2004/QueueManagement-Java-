package org.example.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private final BlockingQueue<Task> tasks;
    private final AtomicInteger waitingPeriod;
    private int clientsApproached;
    private int clientsServed;
    private int totalTimeService;
    private final ArrayList<Task> finishedTasks;
    private Task currentTask;

    public Server() {
        tasks = new LinkedBlockingDeque<>();
        waitingPeriod = new AtomicInteger();
        totalTimeService = 0;
        clientsApproached = 0;
        clientsServed = 0;
        finishedTasks=new ArrayList<>();
    }

    public void addTask(Task newTask) {
        this.tasks.add(newTask);
        newTask.setWaitingTime(waitingPeriod.get());
        this.waitingPeriod.addAndGet(newTask.getServiceTime());
    }

    @Override
    public void run() {
        while (true) {
            try {
                currentTask = tasks.take();
                int time = currentTask.getServiceTime();
                clientsApproached++;
                for (int i = 0; i < time; i++) {
                    Thread.sleep(100);
                    currentTask.decrementServiceTime();
                    waitingPeriod.decrementAndGet();
                }
                totalTimeService+=time;
                clientsServed++;
                finishedTasks.add(currentTask);
                currentTask = null;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public List<Task> getTasks() {
        return List.copyOf(tasks);
    }
    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }
    public Task getCurrentTask() {
        return currentTask;
    }

    public int getTotalTimeService() {
        return totalTimeService;
    }

    public int getClientsApproached() {
        return clientsApproached;
    }

    public int getClientsServed() {
        return clientsServed;
    }

    public ArrayList<Task> getFinishedTasks() {
        return finishedTasks;
    }
}
