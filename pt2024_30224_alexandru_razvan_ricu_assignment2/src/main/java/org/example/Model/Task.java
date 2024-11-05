package org.example.Model;

public class Task {
    private final int arrivalTime;
    private int serviceTime;
    private int waitingTime;
    public Task(int arrivalTime, int serviceTime){
        this.arrivalTime=arrivalTime;
        this.serviceTime=serviceTime;
        this.waitingTime=0;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void decrementServiceTime(){
      //  System.out.println(serviceTime);
        this.serviceTime--;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }
}
