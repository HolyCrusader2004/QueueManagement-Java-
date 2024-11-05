package org.example.BusinessLogic;

import org.example.Model.Server;
import org.example.Model.Task;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private final List<Server>  servers;
    private int maxNoServers;

    private Strategy strategy;

    public Scheduler(int maxNoServers){
        servers = new ArrayList<>();
        this.maxNoServers=maxNoServers;
        for(int i = 0; i<this.maxNoServers;i++){
            Server server=new Server();
            servers.add(server);
            Thread t =new Thread(server);
            t.start();
        }
    }
    public void changeStrategy(SelectionPolicy policy){
        if(policy == SelectionPolicy.SHORTEST_QUEUE){
            strategy = new ConcreteStrategyQueue();
        }
        if(policy == SelectionPolicy.SHORTEST_TIME){
            strategy = new ConcreteStrategyTime();
        }
    }
    public void dispatchTask(Task t){
        strategy.addTask(servers,t);
    }
    public List<Server>getServers(){
        return servers;
    }

    public Strategy getStrategy() {
        return strategy;
    }
}
