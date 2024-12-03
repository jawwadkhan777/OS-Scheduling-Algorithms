package com.schedulingAlgos;

import java.util.*;

// Implementation of first-in-first-out scheduling algorithm
public class FIFOScheduling {
    public static void firstInFirstOut() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter the number of processes (1-4): ");
        int n = sc.nextInt();
        if (n < 1 || n > 4) {
            System.out.println("Invalid number of processes. Please enter between 1 and 4.");
            return;
        }

        List<Process> processes = new ArrayList<>();

        // Input for each process
        for (int i = 1; i <= n; i++) {
            System.out.println("\nProcess " + i + ":");
            System.out.print("Arrival Time: ");
            int arrivalTime = sc.nextInt();

            System.out.print("Execution Time (1-10): ");
            int executionTime = sc.nextInt();
            if (executionTime < 1 || executionTime > 10) {
                System.out.println("Invalid execution time. Please enter between 1 and 10.");
                return;
            }

            System.out.print("Resource Required (0 for No, 1 for Yes): ");
            int resourceFlag = sc.nextInt();

            processes.add(new Process(i, arrivalTime, executionTime, resourceFlag));
        }

        // Sort processes by arrival time
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));

        int currentTime = 0;
        Queue<Process> readyQueue = new LinkedList<>();

        // Gantt chart display
        System.out.println("\nGantt Chart:");
        int irValue = 2001;  // Starting IR value
        int pcValue = 3001;  // Starting PC value
        for (Process p : processes) {
            if (currentTime < p.arrivalTime) {
                currentTime = p.arrivalTime;
            }
            // Check if the process needs I/O resource
            if (p.resourceFlag == 1) {
                System.out.println("P" + p.id + " enters I/O wait state.");
                currentTime++;
//                currentTime += 2; // Simulate resource wait time
            }
            p.responseTime = currentTime - p.arrivalTime;
            p.completionTime = currentTime + p.executionTime;
            p.turnAroundTime = p.completionTime - p.arrivalTime;
            p.waitingTime = p.turnAroundTime - p.executionTime;

            // Set Instruction Register and Program Counter
            p.instructionRegister = irValue;
            p.programCounter = pcValue;
            irValue += 1000;  // Increment IR value for the next process
            pcValue += 1002;  // Increment PC value for the next process

            readyQueue.add(p);

            System.out.print("P" + p.id + " [" + currentTime + " - " + (currentTime + p.executionTime) + "] ");
            currentTime += p.executionTime;
        }

        // Output table
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

        sc.close();
    }
}
