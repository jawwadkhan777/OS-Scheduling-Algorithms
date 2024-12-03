package com.schedulingAlgos;

import java.util.*;

// Implementation of shortest-remaining-time-first scheduling algorithm
public class SRTFScheduling {
    public static void shortestRemainingTimeFirst() {
        Scanner scanner = new Scanner(System.in);

        // Take input for number of processes
        System.out.print("Enter the number of processes (1-4): ");
        int numProcesses = scanner.nextInt();

        if (numProcesses < 1 || numProcesses > 4) {
            System.out.println("Invalid number of processes. Please enter between 1 and 4.");
            return;
        }

        List<Process> processes = new ArrayList<>();
        // Input process details
        for (int i = 1; i <= numProcesses; i++) {
            System.out.println("\nEnter details for Process P" + i);
            System.out.print("Arrival Time: ");
            int arrivalTime = scanner.nextInt();
            System.out.print("Execution Time (1-10): ");
            int executionTime = scanner.nextInt();

            processes.add(new Process(i, arrivalTime, executionTime));

            // Assign random values to Instruction Register and Program Counter
//            process.instructionRegister = 2001 + i * 1000;
//            process.programCounter = 3001 + i * 1002;
//
//            processes.add(process);
        }

        List<String> ganttChart = new ArrayList<>();
        int currentTime = 0;
        int completedProcesses = 0;
        Process currentProcess = null;

        int irValue = 2001;  // Starting IR value
        int pcValue = 3001;  // Starting PC value

        while (completedProcesses < numProcesses) {
            // Select the process with the shortest remaining time that has arrived
            Process shortestRemainingProcess = null;
            for (Process process : processes) {
                if (process.arrivalTime <= currentTime && process.remainingTime > 0 &&
                        (shortestRemainingProcess == null || process.remainingTime < shortestRemainingProcess.remainingTime)) {
                    shortestRemainingProcess = process;
                }
            }

            // If there is no available process, increment time
            if (shortestRemainingProcess == null) {
                currentTime++;
                continue;
            }

            // Preempt if the selected process changes (new shorter process found)
            if (currentProcess != shortestRemainingProcess) {
//                if (currentProcess != null) {
//                    ganttChart.add("P" + currentProcess.id + " [" + (currentTime - 1) + "-" + currentTime + "]");
//                }
                currentProcess = shortestRemainingProcess;
                ganttChart.add("P" + currentProcess.id + " [" + currentTime + "-");

                // Set response time if first time running
                if (currentProcess.responseTime == -1) {
                    currentProcess.responseTime = currentTime - currentProcess.arrivalTime;
                }
            }

            // Execute the current process
            currentProcess.remainingTime--;
            currentTime++;

            // If process finishes execution
            if (currentProcess.remainingTime == 0) {
                currentProcess.completionTime = currentTime;
                currentProcess.turnAroundTime = currentProcess.completionTime - currentProcess.arrivalTime;
                currentProcess.waitingTime = currentProcess.turnAroundTime - currentProcess.executionTime;

                // Set Instruction Register and Program Counter
                currentProcess.instructionRegister = irValue;
                currentProcess.programCounter = pcValue;
                irValue += 1000;  // Increment IR value for the next process
                pcValue += 1002;  // Increment PC value for the next process

                ganttChart.set(ganttChart.size() - 1, ganttChart.get(ganttChart.size() - 1) + (currentTime) + "]");
//                ganttChart.add(currentTime + "]");

                completedProcesses++;

                // Display Gantt chart step and remaining times
                if (completedProcesses < numProcesses) {
                    System.out.println("\nCurrent Gantt Chart: " + String.join(" | ", ganttChart));
                    System.out.println("Remaining Execution Times:");
                    for (Process p : processes) {
                        if (p.remainingTime > 0 && p.arrivalTime <= currentTime) {
                            System.out.println("Process P" + p.id + ": " + p.remainingTime);
                        }
                    }
                }
            }
        }

        // Display final Gantt chart
        System.out.println("\nFinal Gantt Chart:");
        System.out.println(String.join(" | ", ganttChart));


        // Display the process table
        System.out.println("\n\nProcess Table:");
        System.out.printf("%-10s%-15s%-15s%-15s%-15s%-15s%-15s%-15s%n",
                "Process", "Arrival", "Execution", "Completion",
                "Turnaround", "Waiting", "Response", "Instr. Reg / PC");

        for (Process p : processes) {
            System.out.printf("P%-12d%-15d%-15d%-15d%-15d%-15d%-15d%-15s%n",
                    p.id, p.arrivalTime, p.executionTime, p.completionTime,
                    p.turnAroundTime, p.waitingTime, p.responseTime,
                    p.instructionRegister + " / " + p.programCounter);
        }
    }
}
