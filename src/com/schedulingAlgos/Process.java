package com.schedulingAlgos;

public class Process {
    int id;
    int arrivalTime;
    int executionTime;
    int resourceFlag;
    int completionTime;
    int turnAroundTime;
    int waitingTime;
    int responseTime=-1;
    int instructionRegister;
    int programCounter;
    boolean isCompleted = false;
    double responseRatio;

    int remainingTime;

    public Process(int id, int arrivalTime, int executionTime, int resourceFlag) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.executionTime = executionTime;
        this.resourceFlag = resourceFlag;
    }

    public Process(int id, int arrivalTime, int executionTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.executionTime = executionTime;
        this.remainingTime = executionTime;
    }

    // For HRRN scheduling algo.
    public double getResponseRatio(int currentTime) {
        int waitingTime = currentTime - arrivalTime;
        responseRatio = (double) (waitingTime + executionTime) / executionTime;
        return responseRatio;
//        return (double) (waitingTime + executionTime) / executionTime;
    }
}
