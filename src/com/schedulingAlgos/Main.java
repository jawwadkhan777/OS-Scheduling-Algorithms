package com.schedulingAlgos;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("------Welcome to the OS Scheduling Algorithm Hub------");
        System.out.println("CODE INSTRUCTION: 1 for FIFO Scheduling Algo, 2 for SJF Scheduling Algo, 3 for HRRN Scheduling Algo, 4 for SRTF Scheduling ALgo and 5 for RR Scheduling Algo.");

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Scheduling Algo. Code: ");
        int code = scanner.nextInt();

        switch (code) {
            case 1:
                System.out.println("-----FIFO Scheduling-----");
                FIFOScheduling.firstInFirstOut();
                break;
            case 2:
                System.out.println("-----SJF Scheduling-----");
                SJFScheduling.shortestJobFirst();
                break;
            case 3:
                System.out.println("-----HRRN Scheduling-----");
                HRRNScheduling.highestResponseRatioNext();
                break;
            case 4:
                System.out.println("-----SRTF Scheduling-----");
                SRTFScheduling.shortestRemainingTimeFirst();
                break;
            case 5:
                System.out.println("-----Round-Robin Scheduling-----");
                RoundRobinScheduling.roundRobin();
                break;
            default:
                System.out.println("Invalid Input, run again!");
        }

        System.out.println("-------------Thanks For Using-------------");
    }
}
