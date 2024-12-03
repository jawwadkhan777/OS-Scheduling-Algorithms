package com.schedulingAlgos;

import java.util.*;

// Implementation of shortest-job-first scheduling algorithm
public class SJFScheduling {
    public static void shortestJobFirst() {
        Scanner scanner = new Scanner(System.in);

        // Take input for number of processes
        System.out.print("Enter the number of processes (1-4): ");
        int numProcesses = scanner.nextInt();

        if (numProcesses < 1 || numProcesses > 4) {
            System.out.println("Invalid number of processes. Please enter between 1 and 4.");
            return;
        }

        List<Process> processes = new ArrayList<>();
        for (int i = 1; i <= numProcesses; i++) {
            System.out.println("\nEnter details for Process P" + i);
            System.out.print("Arrival Time: ");
            int arrivalTime = scanner.nextInt();
            System.out.print("Execution Time (1-10): ");
            int executionTime = scanner.nextInt();
            System.out.print("Requires Resource (0 for No, 1 for Yes): ");
            int resourceFlag = scanner.nextInt();
            processes.add(new Process(i, arrivalTime, executionTime, resourceFlag));
        }

        // Sort processes based on arrival time
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));

        int currentTime = 0;
        List<Process> readyQueue = new ArrayList<>();
        List<Process> ganttChart = new ArrayList<>();

        System.out.println("\nGantt Chart:");

        int irValue = 2001;  // Starting IR value
        int pcValue = 3001;  // Starting PC value

        while (readyQueue.size() < numProcesses) {
            // Filter out arrived but incomplete processes
            List<Process> availableProcesses = new ArrayList<>();
            for (Process p : processes) {
                if (p.arrivalTime <= currentTime && !p.isCompleted) {
                    availableProcesses.add(p);
                }
            }

            if (availableProcesses.isEmpty()) {
                currentTime++;
                continue;
            }

            // Select the process with the shortest execution time
            Process currentProcess = availableProcesses.stream()
                    .min(Comparator.comparingInt(p -> p.executionTime))
                    .orElse(null);

            if (currentProcess != null) {
                // Check resource requirement
                if (currentProcess.resourceFlag == 1) {
//                    System.out.print("Process P" + currentProcess.id + " requires a resource, so it waits.");
                    System.out.print("P" + currentProcess.id + " enters I/O wait state.");
                    currentTime++;
                }

                // Execute the selected process
                currentProcess.responseTime = currentTime - currentProcess.arrivalTime;
                currentTime += currentProcess.executionTime;
                currentProcess.completionTime = currentTime;
                currentProcess.turnAroundTime = currentProcess.completionTime - currentProcess.arrivalTime;
                currentProcess.waitingTime = currentProcess.turnAroundTime - currentProcess.executionTime;
                currentProcess.isCompleted = true;

                // Set Instruction Register and Program Counter
                currentProcess.instructionRegister = irValue;
                currentProcess.programCounter = pcValue;
                irValue += 1000;  // Increment IR value for the next process
                pcValue += 1002;  // Increment PC value for the next process

                ganttChart.add(currentProcess);
                readyQueue.add(currentProcess);

                System.out.print("P" + currentProcess.id + " | ");
            }
        }

        // Sort readyQueue by process ID to maintain input order in the output
        readyQueue.sort(Comparator.comparingInt(p -> p.id));

        // Display the process table
        System.out.println("\n\nProcess Table:");
        System.out.printf("%-10s%-15s%-15s%-15s%-15s%-15s%-15s%-15s%-15s%n",
                "Process", "Arrival", "Execution", "Completion",
                "Turnaround", "Waiting", "Response", "Resource", "Instr. Reg / PC");

        for (Process p : readyQueue) {
            System.out.printf("P%-12d%-15d%-15d%-15d%-15d%-15d%-15d%-15s%-15s%n",
                    p.id, p.arrivalTime, p.executionTime, p.completionTime,
                    p.turnAroundTime, p.waitingTime, p.responseTime,
                    (p.resourceFlag == 1 ? "Yes" : "No"),
                    p.instructionRegister + " / " + p.programCounter);
        }

        scanner.close();
    }
}

