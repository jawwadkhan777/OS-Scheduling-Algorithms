package com.schedulingAlgos;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

// Implementation of round-robin scheduling algorithm
public class RoundRobinScheduling {
    public static void roundRobin() {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        // Input number of processes
        System.out.print("How many processes (1-4): ");
        int n = scanner.nextInt();
        while (!(n > 0 && n < 5)) {
            System.out.println("Incorrect input");
            System.out.print("How many processes (1-4): ");
            n = scanner.nextInt();
        }

        List<List<Integer>> procs = new ArrayList<>();
        List<Boolean> procsRI = new ArrayList<>();

        // For all processes
        for (int i = 0; i < n; i++) {
            System.out.println("\nProcess " + (i + 1));

            // Execution time input
            System.out.print("Execution time (1-10): ");
            int e = scanner.nextInt();
            while (!(e > 0 && e <= 10)) {
                System.out.println("Incorrect input");
                System.out.print("Execution time (1-10): ");
                e = scanner.nextInt();
            }

            // Resource information input
            System.out.print("Resource Information (0-1): ");
            int ri = scanner.nextInt();
            while (!(ri >= 0 && ri <= 1)) {
                System.out.println("Incorrect input");
                System.out.print("Resource Information (0-1): ");
                ri = scanner.nextInt();
            }

            procsRI.add(ri == 1);

            // Random instructions for execution
            List<Integer> temp = new ArrayList<>();
            for (int j = 0; j < e; j++) {
                temp.add(random.nextInt(21));
            }
            procs.add(temp);
        }

        // Quantum size input
        System.out.print("\nQuantum size (1-3): ");
        int q = scanner.nextInt();
        while (!(q > 0 && q <= 3)) {
            System.out.println("Incorrect input");
            System.out.print("Quantum size (1-3): ");
            q = scanner.nextInt();
        }

        // PCB Table
        System.out.println();
        System.out.println("Name\tArrival \tExecution");
        for (int i = 0; i < procs.size(); i++) {
            System.out.println("p" + (i + 1) + "\t\t\t" + i + "\t\t\t" + procs.get(i).size());
        }

        // PCB Implementation
        int noOfProcs = procs.size();
        int[] arrivalTimes = new int[noOfProcs];
        int[] executionTimes = new int[noOfProcs];
        int[] remainingTime = new int[noOfProcs];
        int[][] psw = new int[noOfProcs][2];
        int currentTime = 0;
        int[] waitingProcs = new int[noOfProcs];
        String[] finishTime = new String[noOfProcs];
        int[] quantumCount = new int[noOfProcs];
        String[] state = new String[noOfProcs];
        List<Integer> queue = new ArrayList<>();
        int[] IR = {0, 0};

        // Initialization
        for (int i = 0; i < noOfProcs; i++) {
            arrivalTimes[i] = i;
            executionTimes[i] = procs.get(i).size();
            remainingTime[i] = executionTimes[i];
            psw[i][0] = 0;
            psw[i][1] = 0;
            waitingProcs[i] = 0;
            finishTime[i] = "-";
            quantumCount[i] = 0;
            state[i] = "Ready";
            queue.add(i);
        }

        state[IR[0]] = "Running";

        // Main scheduling loop
        List<List<Object>> processDetailList = new ArrayList<>();
        while (true) {
            boolean anyProcessRemaining = false;
            for (int rt : remainingTime) {
                if (rt > 0) {
                    anyProcessRemaining = true;
                    break;
                }
            }

            if (!anyProcessRemaining) {
                break;
            }

            state[IR[0]] = "Running";
            quantumCount[IR[0]]++;
            remainingTime[IR[0]]--;
            currentTime++;

            if (remainingTime[IR[0]] == 0) {
                finishTime[IR[0]] = String.valueOf(currentTime);
                state[IR[0]] = "Exit";

                List<Object> tempDetail = new ArrayList<>();
                tempDetail.add(currentTime);
                tempDetail.add(IR.clone());

                for (int i = 0; i < noOfProcs; i++) {
                    List<Object> tempProcess = new ArrayList<>();
                    tempProcess.add(arrivalTimes[i]);
                    tempProcess.add(executionTimes[i]);
                    tempProcess.add(finishTime[i]);
                    tempProcess.add(procsRI.get(i));
                    tempProcess.add(state[i]);
                    tempProcess.add(psw[i].clone());
                    tempDetail.add(tempProcess);
                }

                processDetailList.add(tempDetail);
            }

            // Blocking mechanism for resource information
            if (remainingTime[IR[0]] + 1 == executionTimes[IR[0]] && procsRI.get(IR[0])) {
                state[IR[0]] = "Block";
                psw[IR[0]][0] = IR[1] + (IR[0] + 1) * 1000;
                psw[IR[0]][1] = IR[1] + (IR[0] + 1) * 1000 + 1;
            }

            // Check block state and waiting time
            for (int i = 0; i < noOfProcs; i++) {
                if (state[i].equals("Block") && waitingProcs[i] < 2) {
                    waitingProcs[i]++;
                } else if (state[i].equals("Block") && waitingProcs[i] == 2) {
                    state[i] = "Ready";
                    psw[i][0] = 0;
                    psw[i][1] = 0;
                    waitingProcs[i] = 0;
                }
            }

            if (quantumCount[IR[0]] < q && state[IR[0]].equals("Running")) {
                IR[1]++;
                continue;
            } else if (state[IR[0]].equals("Exit")) {
                queue.remove(Integer.valueOf(IR[0]));
            } else {
                if (state[IR[0]].equals("Running")) {
                    state[IR[0]] = "Ready";
                }
                quantumCount[IR[0]] = 0;
                queue.remove(Integer.valueOf(IR[0]));
                queue.add(IR[0]);
            }

            // Handle the scheduling queue
            boolean notIdle = true;
            while (notIdle && !queue.isEmpty()) {
                for (int p : queue) {
                    if (state[p].equals("Ready")) {
                        IR[0] = p;
                        IR[1] = executionTimes[p] - remainingTime[p];
                        notIdle = false;
                        break;
                    }
                }

                if (notIdle) {
                    currentTime++;
                    for (int i = 0; i < noOfProcs; i++) {
                        if (state[i].equals("Block") && waitingProcs[i] < 2) {
                            waitingProcs[i]++;
                        } else if (state[i].equals("Block") && waitingProcs[i] == 2) {
                            state[i] = "Ready";
                            psw[i][0] = 0;
                            psw[i][1] = 0;
                            waitingProcs[i] = 0;
                        }
                    }
                }
            }
        }

        // Printing the graph
        System.out.println("-----------------------------------------------------------");
        System.out.println("Graph");
        System.out.println("-----------------------------------------------------------");
        for (List<Object> pd : processDetailList) {
            int[] ir = (int[]) pd.get(1);
            System.out.print("p" + (ir[0] + 1) + "|");
        }

        // Printing the details
        for (List<Object> pd : processDetailList) {
            System.out.println("\n-----------------------------------------------------------");
            System.out.println("Quantum " + pd.get(0));
            System.out.println("-----------------------------------------------------------");
            int[] ir = (int[]) pd.get(1);
            System.out.println("IR = " + (ir[1] + (ir[0] + 1) * 1000));
            if (processDetailList.indexOf(pd) < processDetailList.size() - 1) {
                int[] nextIr = (int[]) processDetailList.get(processDetailList.indexOf(pd) + 1).get(1);
                System.out.println("PC = " + (nextIr[1] + (nextIr[0] + 1) * 1000));
            } else {
                System.out.println("PC = -");
            }
            for (int j = 2; j < pd.size(); j++) {
                List<Object> pcb = (List<Object>) pd.get(j);
                System.out.println("\nPCB p" + ((int)(pcb.get(0))+1));
                System.out.println("Arrival Time:\t\t" + pcb.get(0));
                System.out.println("Execution Time:\t\t" + pcb.get(1));
                System.out.println("Finish Time:\t\t" + pcb.get(2));
                System.out.println("Resource Info:\t\t" + (boolean) pcb.get(3));
                System.out.println("State:\t\t\t" + pcb.get(4));
                int[] pswArr = (int[]) pcb.get(5);
                System.out.println("PSW: " + pswArr[0] + " " + pswArr[1]);
            }
        }
    }
}
