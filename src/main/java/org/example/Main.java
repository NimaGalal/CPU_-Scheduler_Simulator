package org.example;

import java.util.*;

public class Main {
    // Inner classes for test data structures (Process removed to use the actual Process class)
    static class ProcessResult {
        int waitingTime;
        int turnaroundTime;

        ProcessResult(int waitingTime, int turnaroundTime) {
            this.waitingTime = waitingTime;
            this.turnaroundTime = turnaroundTime;
        }
    }

    static class ExpectedScheduler {
        List<String> executionOrder;
        Map<String, ProcessResult> processResults;
        double avgWaitingTime;
        double avgTurnaroundTime;

        ExpectedScheduler(List<String> executionOrder, Map<String, ProcessResult> processResults, double avgWaitingTime, double avgTurnaroundTime) {
            this.executionOrder = executionOrder;
            this.processResults = processResults;
            this.avgWaitingTime = avgWaitingTime;
            this.avgTurnaroundTime = avgTurnaroundTime;
        }
    }

    static class TestCase {
        String name;
        int contextSwitch;
        int rrQuantum;
        int agingInterval;
        List<Process> processes;
        Map<String, ExpectedScheduler> expected; // Keys: "Priority", "RR"

        TestCase(String name, int contextSwitch, int rrQuantum, int agingInterval, List<Process> processes, Map<String, ExpectedScheduler> expected) {
            this.name = name;
            this.contextSwitch = contextSwitch;
            this.rrQuantum = rrQuantum;
            this.agingInterval = agingInterval;
            this.processes = processes;
            this.expected = expected;
        }
    }

    // Method to compare SchedulerResult with expected data
    private static boolean compareResults(SchedulerResult actual, ExpectedScheduler expected) {
        boolean pass = true;
        double epsilon = 0.01; // For double comparisons

        // Check if process counts match
        if (actual.getWaitingTimes().size() != expected.processResults.size()) {
            System.out.println("  Process count mismatch: expected " + expected.processResults.size() + ", got " + actual.getWaitingTimes().size());
            pass = false;
        }

        // Check execution order
        if (!actual.getExecutionOrder().equals(expected.executionOrder)) {
            System.out.println("  Execution order mismatch: expected " + expected.executionOrder + ", got " + actual.getExecutionOrder());
            pass = false;
        }

        // Check waiting times
        for (String processName : expected.processResults.keySet()) {
            int expectedWaiting = expected.processResults.get(processName).waitingTime;
            int actualWaiting = actual.getWaitingTimes().getOrDefault(processName, -1);
            if (actualWaiting != expectedWaiting) {
                System.out.println("  Waiting time mismatch for " + processName + ": expected " + expectedWaiting + ", got " + actualWaiting);
                pass = false;
            }
        }

        // Check turnaround times
        for (String processName : expected.processResults.keySet()) {
            int expectedTurnaround = expected.processResults.get(processName).turnaroundTime;
            int actualTurnaround = actual.getTurnaroundTimes().getOrDefault(processName, -1);
            if (actualTurnaround != expectedTurnaround) {
                System.out.println("  Turnaround time mismatch for " + processName + ": expected " + expectedTurnaround + ", got " + actualTurnaround);
                pass = false;
            }
        }

        // Check average waiting time
        if (Math.abs(actual.getAvgWaitingTime() - expected.avgWaitingTime) > epsilon) {
            System.out.println("  Average waiting time mismatch: expected " + expected.avgWaitingTime + ", got " + actual.getAvgWaitingTime());
            pass = false;
        }

        // Check average turnaround time
        if (Math.abs(actual.getAvgTurnaroundTime() - expected.avgTurnaroundTime) > epsilon) {
            System.out.println("  Average turnaround time mismatch: expected " + expected.avgTurnaroundTime + ", got " + actual.getAvgTurnaroundTime());
            pass = false;
        }

        return pass;
    }

    public static void main(String[] args) {
        // Hardcoded test cases (transcribed from your JSON files)
        List<TestCase> testCases = new ArrayList<>();

        // Test Case 1
        List<Process> processes1 = Arrays.asList(
            new Process("P1", 0, 8, 3, 0),
            new Process("P2", 1, 4, 1, 0),
            new Process("P3", 2, 2, 4, 0),
            new Process("P4", 3, 1, 2, 0),
            new Process("P5", 4, 3, 5, 0)
        );
        Map<String, ProcessResult> priResults1 = new HashMap<>();
        priResults1.put("P1", new ProcessResult(9, 17));
        priResults1.put("P2", new ProcessResult(1, 5));
        priResults1.put("P3", new ProcessResult(16, 18));
        priResults1.put("P4", new ProcessResult(6, 7));
        priResults1.put("P5", new ProcessResult(17, 20));
        Map<String, ProcessResult> rrResults1 = new HashMap<>();
        rrResults1.put("P1", new ProcessResult(19, 27));
        rrResults1.put("P2", new ProcessResult(14, 18));
        rrResults1.put("P3", new ProcessResult(4, 6));
        rrResults1.put("P4", new ProcessResult(9, 10));
        rrResults1.put("P5", new ProcessResult(17, 20));
        Map<String, ExpectedScheduler> expected1 = new HashMap<>();
        expected1.put("Priority", new ExpectedScheduler(Arrays.asList("P1", "P2", "P1", "P4", "P1", "P3", "P5"), priResults1, 9.8, 13.4));
        expected1.put("RR", new ExpectedScheduler(Arrays.asList("P1", "P2", "P3", "P1", "P4", "P5", "P2", "P1", "P5", "P1"), rrResults1, 12.6, 16.2));
        testCases.add(new TestCase("Test Case 1: Basic mixed arrivals", 1, 2, 5, processes1, expected1));

        // Test Case 2
        List<Process> processes2 = Arrays.asList(
            new Process("P1", 0, 6, 3, 0),
            new Process("P2", 0, 3, 1, 0),
            new Process("P3", 0, 8, 2, 0),
            new Process("P4", 0, 4, 4, 0),
            new Process("P5", 0, 2, 5, 0)
        );
        Map<String, ProcessResult> priResults2 = new HashMap<>();
        priResults2.put("P1", new ProcessResult(12, 18));
        priResults2.put("P2", new ProcessResult(0, 3));
        priResults2.put("P3", new ProcessResult(14, 22));
        priResults2.put("P4", new ProcessResult(23, 27));
        priResults2.put("P5", new ProcessResult(28, 30));
        Map<String, ProcessResult> rrResults2 = new HashMap<>();
        rrResults2.put("P1", new ProcessResult(16, 22));
        rrResults2.put("P2", new ProcessResult(4, 7));
        rrResults2.put("P3", new ProcessResult(23, 31));
        rrResults2.put("P4", new ProcessResult(24, 28));
        rrResults2.put("P5", new ProcessResult(16, 18));
        Map<String, ExpectedScheduler> expected2 = new HashMap<>();
        expected2.put("Priority", new ExpectedScheduler(Arrays.asList("P2", "P3", "P1", "P3", "P1", "P3", "P4", "P5"), priResults2, 15.4, 20.0));
        expected2.put("RR", new ExpectedScheduler(Arrays.asList("P1", "P2", "P3", "P4", "P5", "P1", "P3", "P4", "P3"), rrResults2, 16.6, 21.2));
        testCases.add(new TestCase("Test Case 2: All processes arrive at time 0", 1, 3, 5, processes2, expected2));

        // Test Case 3
        List<Process> processes3 = Arrays.asList(
            new Process("P1", 0, 10, 5, 0),
            new Process("P2", 2, 5, 1, 0),
            new Process("P3", 5, 3, 2, 0),
            new Process("P4", 8, 7, 1, 0),
            new Process("P5", 10, 2, 3, 0)
        );
        Map<String, ProcessResult> priResults3 = new HashMap<>();
        priResults3.put("P1", new ProcessResult(17, 27));
        priResults3.put("P2", new ProcessResult(1, 6));
        priResults3.put("P3", new ProcessResult(5, 8));
        priResults3.put("P4", new ProcessResult(16, 23));
        priResults3.put("P5", new ProcessResult(22, 24));
        Map<String, ProcessResult> rrResults3 = new HashMap<>();
        rrResults3.put("P1", new ProcessResult(21, 31));
        rrResults3.put("P2", new ProcessResult(18, 23));
        rrResults3.put("P3", new ProcessResult(10, 13));
        rrResults3.put("P4", new ProcessResult(20, 27));
        rrResults3.put("P5", new ProcessResult(16, 18));
        Map<String, ExpectedScheduler> expected3 = new HashMap<>();
        expected3.put("Priority", new ExpectedScheduler(Arrays.asList("P1", "P2", "P4", "P3", "P4", "P1", "P4", "P5"), priResults3, 12.2, 17.6));
        expected3.put("RR", new ExpectedScheduler(Arrays.asList("P1", "P2", "P1", "P3", "P4", "P2", "P5", "P1", "P4"), rrResults3, 17.0, 22.4));
        testCases.add(new TestCase("Test Case 3: Varied burst times with starvation risk", 1, 4, 4, processes3, expected3));

        // Test Case 4
        List<Process> processes4 = Arrays.asList(
            new Process("P1", 0, 12, 2, 0),
            new Process("P2", 4, 9, 3, 0),
            new Process("P3", 8, 15, 1, 0),
            new Process("P4", 12, 6, 4, 0),
            new Process("P5", 16, 11, 2, 0),
            new Process("P6", 20, 5, 5, 0)
        );
        Map<String, ProcessResult> priResults4 = new HashMap<>();
        priResults4.put("P1", new ProcessResult(8, 20));
        priResults4.put("P2", new ProcessResult(18, 27));
        priResults4.put("P3", new ProcessResult(21, 36));
        priResults4.put("P4", new ProcessResult(34, 40));
        priResults4.put("P5", new ProcessResult(38, 49));
        priResults4.put("P6", new ProcessResult(47, 52));
        Map<String, ProcessResult> rrResults4 = new HashMap<>();
        rrResults4.put("P1", new ProcessResult(38, 50));
        rrResults4.put("P2", new ProcessResult(26, 35));
        rrResults4.put("P3", new ProcessResult(58, 73));
        rrResults4.put("P4", new ProcessResult(49, 55));
        rrResults4.put("P5", new ProcessResult(57, 68));
        rrResults4.put("P6", new ProcessResult(32, 37));
        Map<String, ExpectedScheduler> expected4 = new HashMap<>();
        expected4.put("Priority", new ExpectedScheduler(Arrays.asList("P1", "P3", "P1", "P2", "P3", "P4", "P5", "P6"), priResults4, 27.67, 37.33));
        expected4.put("RR", new ExpectedScheduler(Arrays.asList("P1", "P2", "P1", "P3", "P4", "P2", "P5", "P1", "P6", "P3", "P4", "P5", "P3", "P5"), rrResults4, 43.33, 53.0));
        testCases.add(new TestCase("Test Case 4: Large bursts with gaps in arrivals", 2, 5, 6, processes4, expected4));

        // Test Case 5
        List<Process> processes5 = Arrays.asList(
            new Process("P1", 0, 3, 3, 0),
            new Process("P2", 1, 2, 1, 0),
            new Process("P3", 2, 4, 2, 0),
            new Process("P4", 3, 1, 4, 0),
            new Process("P5", 4, 3, 5, 0)
        );
        Map<String, ProcessResult> priResults5 = new HashMap<>();
        priResults5.put("P1", new ProcessResult(7, 10));
        priResults5.put("P2", new ProcessResult(1, 3));
        priResults5.put("P3", new ProcessResult(8, 12));
        priResults5.put("P4", new ProcessResult(12, 13));
        priResults5.put("P5", new ProcessResult(13, 16));
        Map<String, ProcessResult> rrResults5 = new HashMap<>();
        rrResults5.put("P1", new ProcessResult(7, 10));
        rrResults5.put("P2", new ProcessResult(2, 4));
        rrResults5.put("P3", new ProcessResult(12, 16));
        rrResults5.put("P4", new ProcessResult(8, 9));
        rrResults5.put("P5", new ProcessResult(13, 16));
        Map<String, ExpectedScheduler> expected5 = new HashMap<>();
        expected5.put("Priority", new ExpectedScheduler(Arrays.asList("P1", "P2", "P1", "P3", "P1", "P3", "P4", "P5"), priResults5, 8.2, 10.8));
        expected5.put("RR", new ExpectedScheduler(Arrays.asList("P1", "P2", "P3", "P1", "P4", "P5", "P3", "P5"), rrResults5, 8.4, 11.0));
        testCases.add(new TestCase("Test Case 5: Short bursts with high frequency", 1, 2, 3, processes5, expected5));

        // Test Case 6
        List<Process> processes6 = Arrays.asList(
            new Process("P1", 0, 14, 4, 0),
            new Process("P2", 3, 7, 2, 0),
            new Process("P3", 6, 10, 5, 0),
            new Process("P4", 9, 5, 1, 0),
            new Process("P5", 12, 8, 3, 0),
            new Process("P6", 15, 4, 6, 0)
        );
        Map<String, ProcessResult> priResults6 = new HashMap<>();
        priResults6.put("P1", new ProcessResult(16, 30));
        priResults6.put("P2", new ProcessResult(7, 14));
        priResults6.put("P3", new ProcessResult(25, 35));
        priResults6.put("P4", new ProcessResult(29, 34));
        priResults6.put("P5", new ProcessResult(32, 40));
        priResults6.put("P6", new ProcessResult(38, 42));
        Map<String, ProcessResult> rrResults6 = new HashMap<>();
        rrResults6.put("P1", new ProcessResult(44, 58));
        rrResults6.put("P2", new ProcessResult(18, 25));
        rrResults6.put("P3", new ProcessResult(45, 55));
        rrResults6.put("P4", new ProcessResult(36, 41));
        rrResults6.put("P5", new ProcessResult(35, 43));
        rrResults6.put("P6", new ProcessResult(24, 28));
        Map<String, ExpectedScheduler> expected6 = new HashMap<>();
        expected6.put("Priority", new ExpectedScheduler(Arrays.asList("P1", "P2", "P4", "P2", "P4", "P1", "P3", "P4", "P5", "P6"), priResults6, 24.6, 32.6));
        expected6.put("RR", new ExpectedScheduler(Arrays.asList("P1", "P2", "P1", "P3", "P4", "P2", "P5", "P1", "P6", "P3", "P4", "P5", "P1", "P3"), rrResults6, 33.67, 41.67));
        testCases.add(new TestCase("Test Case 6: Mixed scenario - comprehensive test", 1, 4, 5, processes6, expected6));

        // Run the tests
        for (TestCase testCase : testCases) {
            System.out.println("----------------- " + testCase.name + " -----------------");
            Scheduler scheduler = new Scheduler(testCase.processes, testCase.contextSwitch, testCase.rrQuantum, testCase.agingInterval);

            // Test Priority
            SchedulerResult priorityResult = scheduler.runPriority();
            boolean priorityPass = compareResults(priorityResult, testCase.expected.get("Priority"));
            System.out.println("Priority: " + (priorityPass ? "PASS" : "FAIL"));

            // Test RoundRobin
            SchedulerResult rrResult = RoundRobinScheduler.schedule(testCase.processes, testCase.rrQuantum, testCase.contextSwitch);
            boolean rrPass = compareResults(rrResult, testCase.expected.get("RR"));
            System.out.println("RoundRobin: " + (rrPass ? "PASS" : "FAIL"));

            System.out.println("-------------------------------------------------");
        }
    }
}