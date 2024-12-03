package com.schedulingAlgos;

import java.util.*;

// Implementation of highest-response-ratio-next scheduling algorithm
public class HRRNScheduling {
    public static void highestResponseRatioNext() {
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
            System.out.print("Requires Resource (0 for No, 1 for Yes): ");
            int resourceFlag = scanner.nextInt();
            processes.add(new Process(i, arrivalTime, executionTime, resourceFlag));
        }

        // Sort processes based on arrival time
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));

        int currentTime = 0;
        List<Process> ganttChart = new ArrayList<>();
        List<String> responseRatioLogs = new ArrayList<>();  // To store response ratio calculations


        System.out.println("\nGantt Chart:");

        int irValue = 2001;  // Starting IR value
        int pcValue = 3001;  // Starting PC value

        while (ganttChart.size() < numProcesses) {
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

            // Calculate response ratios only if there's more than one available process
            if (availableProcesses.size() > 1) {
                StringBuilder ratioLog = new StringBuilder("Response Ratios at Time " + currentTime + ":\n");
                for (Process p : availableProcesses) {
                    double ratio = p.getResponseRatio(currentTime);
                    ratioLog.append(String.format("Process P%d: %.2f\n", p.id, ratio));
                }
                responseRatioLogs.add(ratioLog.toString());
            }

            // Select the process with the highest response ratio if multiple are available
            int finalCurrentTime = currentTime;
            Process currentProcess = availableProcesses.size() == 1
                    ? availableProcesses.get(0)
                    : availableProcesses.stream().max(Comparator.comparingDouble(p -> p.getResponseRatio(finalCurrentTime))).orElse(null);


            if (currentProcess != null) {
                // Check resource requirement
                if (currentProcess.resourceFlag == 1) {
//                    System.out.println("Process P" + currentProcess.id + " requires a resource, so it waits.");
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

                System.out.print("P" + currentProcess.id + " | ");
            }
        }

        // Sort gantt chart by process ID to maintain input order in the output
        ganttChart.sort(Comparator.comparingInt(p -> p.id));


        // Display response ratios after Gantt chart
        System.out.println("\n\nResponse Ratios:");
        for (String log : responseRatioLogs) {
            System.out.println(log);
            System.out.println("--------------------------");  // Separator between blocks
        }

        // Display the process table
        System.out.println("\n\nProcess Table:");
        System.out.printf("%-10s%-15s%-15s%-15s%-15s%-15s%-15s%-15s%-15s%n",
                "Process", "Arrival", "Execution", "Completion",
                "Turnaround", "Waiting", "Response", "Resource", "Instr. Reg / PC");

        for (Process p : ganttChart) {
            System.out.printf("P%-12d%-15d%-15d%-15d%-15d%-15d%-15d%-15s%-15s%n",
                    p.id, p.arrivalTime, p.executionTime, p.completionTime,
                    p.turnAroundTime, p.waitingTime, p.responseTime,
                    (p.resourceFlag == 1 ? "Yes" : "No"),
                    p.instructionRegister + " / " + p.programCounter);
        }

        scanner.close();
    }
}