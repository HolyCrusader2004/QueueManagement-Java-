package org.example.BusinessLogic;

import org.example.GUI.SimulationFrame;
import org.example.Model.Server;
import org.example.Model.Task;

import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import java.io.BufferedWriter;

import java.util.List;
public class SimulationManager implements Runnable {
    public int timeLimit;
    public int minArrivalTime;
    public int maxArrivalTime;
    public int maxProcessingTime;
    public int minProcessingTime;
    public int numberOfServers;
    public int numberOfClients ;
    public SelectionPolicy selectionPolicy;
    private final Scheduler scheduler;
    private final SimulationFrame frame;
    private final List<Task> generatedTasks;

   public SimulationManager(JTextField[] textFields,SelectionPolicy policy){
       initialize(textFields);
       this.selectionPolicy=policy;
       generatedTasks = new ArrayList<>();
       scheduler = new Scheduler(numberOfServers);
       scheduler.changeStrategy(selectionPolicy);
       System.out.println(scheduler.getStrategy());
       generateNRandomTasks();
       frame=new SimulationFrame("Simulation Frame",generatedTasks,numberOfServers,scheduler.getServers());
   }

    private void generateNRandomTasks(){
        Random rand = new Random();
        for(int i = 0; i < numberOfClients; i++){
            int newArrivalTime = rand.nextInt(maxArrivalTime-minArrivalTime+1)+minArrivalTime;
            int newServiceTime = rand.nextInt(maxProcessingTime-minProcessingTime+1)+minProcessingTime;
            Task newTask = new Task(newArrivalTime,newServiceTime);
            generatedTasks.add(newTask);
        }
        generatedTasks.sort(Comparator.comparingInt(Task::getArrivalTime));
    }

    @Override
    public void run() {
        int currentTime = 0, empty;
        while (currentTime < timeLimit) {
            currentTime++;
            Iterator<Task> iterator = generatedTasks.iterator();
            while (iterator.hasNext()) {
                Task t = iterator.next();
                if (t.getArrivalTime() == currentTime) {
                    scheduler.dispatchTask(t);
                    iterator.remove();
                }
            }
            writeServerConditionsToFile(generatedTasks,scheduler.getServers(),currentTime);
            frame.update(generatedTasks);
            frame.updatePeakHour(scheduler.getServers(),currentTime);
            frame.timePassedField.setText(""+currentTime);
            if(generatedTasks.isEmpty()){
                empty = 1;
                for(Server server:scheduler.getServers()){
                    if(!server.getTasks().isEmpty() || server.getCurrentTask()!=null){
                        empty = 0;
                        break;
                    }
                }
                if(empty == 1) {
                    break;
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        updateInfoPanel();
    }

    public void initialize(JTextField[] input){
        timeLimit = input[6].getText().isEmpty() ? 0 : Integer.parseInt(input[6].getText());
        minArrivalTime = input[3].getText().isEmpty() ? 0 : Integer.parseInt(input[3].getText());
        maxArrivalTime = input[2].getText().isEmpty() ? 0 : Integer.parseInt(input[2].getText());
        minProcessingTime = input[5].getText().isEmpty() ? 0 : Integer.parseInt(input[5].getText());
        maxProcessingTime = input[4].getText().isEmpty() ? 0 : Integer.parseInt(input[4].getText());
        numberOfServers = input[1].getText().isEmpty() ? 0 : Integer.parseInt(input[1].getText());
        numberOfClients = input[0].getText().isEmpty() ? 0 : Integer.parseInt(input[0].getText());
    }

    public void writeServerConditionsToFile(List<Task> generatedTasks, List<Server> servers, int time) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("task_info.txt", true))) {
            int i = 0;
            writer.write(System.lineSeparator());
            writer.write("Time: " + time + System.lineSeparator());
            writer.write("Waiting clients: ");
            for (Task t : generatedTasks) {
                writer.write(" (" + t.getArrivalTime() + " " + t.getServiceTime() + ") ");
            }
            writer.write(System.lineSeparator());
            for (Server serv : servers) {
                writer.write("Queue " + i + ":");
                if (serv.getTasks().isEmpty() && serv.getCurrentTask() == null) {
                    writer.write(" (closed)");
                } else {
                    if (serv.getCurrentTask() != null) {
                        Task currentTask = serv.getCurrentTask();
                        writer.write(" (" + currentTask.getArrivalTime() + " " + currentTask.getServiceTime() + ") ");
                    }
                    for (Task t : serv.getTasks()) {
                        writer.write(" (" + t.getArrivalTime() + " " + t.getServiceTime() + ") ");
                    }
                }
                writer.write(System.lineSeparator());
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void updateInfoPanel(){
        int totalServiceTime = 0, clientsApproached = 0,totalWaitingTime=0,clientsServed=0;
        for(Server server:scheduler.getServers()) {
            totalServiceTime += server.getTotalTimeService();
            clientsApproached += server.getClientsApproached();
            clientsServed+=server.getClientsServed();
            for(Task task: server.getFinishedTasks()){
                totalWaitingTime+=task.getWaitingTime();
            }
            if(server.getCurrentTask()!=null){
                totalWaitingTime+=server.getCurrentTask().getWaitingTime();
            }
        }
        writeStatistic((float) totalServiceTime / clientsServed, (float) totalWaitingTime /clientsApproached,frame.peakHour);
        frame.avgServiceField.setText(""+(float)totalServiceTime/clientsServed);
        frame.avgWaitingField.setText(""+(float)totalWaitingTime/clientsApproached);
    }
    public void writeStatistic(float avgServ,float avgWait,int peakHr){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("task_info.txt", true))) {
            writer.write(System.lineSeparator());
            writer.write("Peak hour: " + peakHr + System.lineSeparator());
            writer.write("Average Service Time: " + avgServ + System.lineSeparator());
            writer.write("Average Waiting Time: " + avgWait + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
