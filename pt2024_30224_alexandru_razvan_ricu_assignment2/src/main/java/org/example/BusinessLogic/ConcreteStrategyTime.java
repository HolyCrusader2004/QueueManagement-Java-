package org.example.BusinessLogic;

import org.example.Model.Server;
import org.example.Model.Task;

import java.util.List;

public class ConcreteStrategyTime implements Strategy{
    @Override
    public void addTask(List<Server> servers, Task t) {
        int min = 999999;
        for(Server server:servers){
            if(server.getWaitingPeriod().intValue()<min){
                min = server.getWaitingPeriod().intValue();
            }
        }
        for(Server server:servers){
            if(server.getWaitingPeriod().intValue() == min){
                server.addTask(t);
                break;
            }
        }
    }
}
