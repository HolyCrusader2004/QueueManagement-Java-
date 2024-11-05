package org.example.BusinessLogic;

import org.example.Model.Server;
import org.example.Model.Task;

import java.util.List;

public class ConcreteStrategyQueue implements Strategy{
    @Override
    public void addTask(List<Server> servers, Task t) {
        int minim = 999999;
        Server aux=null;
        for(Server server:servers){
            if(server.getCurrentTask()==null && server.getTasks().isEmpty()){
                aux=server;
                break;
            }
            if(server.getTasks().size()<minim){
                minim = server.getTasks().size();
                aux=server;
            }
        }
        if(aux!=null) {
            aux.addTask(t);
        }
    }
}
