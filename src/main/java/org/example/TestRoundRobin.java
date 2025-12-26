package org.example;

import java.util.*;

public class TestRoundRobin {
    public static void main(String[] args) {
        System.out.println("=== Round Robin Scheduler - Test Suite ===");
        System.out.println("Testing with example from assignment...\n");
        
        // The test case
        List<Process> processes = new ArrayList<>();
        processes.add(new Process("P1", 0, 17, 4, 7));
        processes.add(new Process("P2", 2, 6, 7, 9));
        processes.add(new Process("P3", 5, 11, 3, 4));
        processes.add(new Process("P4", 15, 4, 6, 6));
        int quantum = 4;
        int contextSwitch = 1;
        
        System.out.println("Test Parameters:");
        System.out.println("- Quantum: " + quantum);
        System.out.println("- Context Switch: " + contextSwitch);
        System.out.println("- Processes: 4 (P1, P2, P3, P4)");
        System.out.println("\n" + "=".repeat(50));
        
        // Run the scheduler
        System.out.println("\nRunning Round Robin Scheduler...");
        SchedulerResult result = RoundRobinScheduler.schedule(processes, quantum, contextSwitch);
        
        System.out.println("\n" + "=".repeat(50));
        System.out.println("TEST RESULTS:");
        System.out.println("=".repeat(50));
        
        // Display results
        result.printResults();
    }
}