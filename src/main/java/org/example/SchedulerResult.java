package org.example;

import java.util.*;

public class SchedulerResult {

        private String schedulerName;
        List<String> executionOrder;
        private Map<String, Integer> waitingTimes;
        private Map<String, Integer> turnaroundTimes;
        private double avgWaitingTime;
        private double avgTurnaroundTime;
        private List<String> quantumHistory;

        public SchedulerResult(String schedulerName) {
            this.schedulerName = schedulerName;
            this.executionOrder = new ArrayList<>();
            this.waitingTimes = new HashMap<>();
            this.turnaroundTimes = new HashMap<>();
            this.quantumHistory = new ArrayList<>();
            this.avgWaitingTime = 0;
            this.avgTurnaroundTime = 0;
        }


        public void addToExecutionOrder(String processName) {
            executionOrder.add(processName);
        }


        public void addProcessTimes(String processName, int waitingTime, int turnaroundTime) {
            waitingTimes.put(processName,waitingTime);
            turnaroundTimes.put(processName,turnaroundTime);
        }


        public void addQuantumHistory(String update) {
            quantumHistory.add(update);
        }


        public void calculateAverages() {
            int totalWaiting = 0;
            int totalTurnaround = 0;
            int count = waitingTimes.size();


            for (String processName : waitingTimes.keySet()) {
                totalWaiting = totalWaiting + waitingTimes.get(processName);
            }


            for (String processName : turnaroundTimes.keySet()) {
                totalTurnaround = totalTurnaround + turnaroundTimes.get(processName);
            }


            if (count > 0) {
                avgWaitingTime = (double) totalWaiting/count;
                avgTurnaroundTime = (double) totalTurnaround/count;
            }
        }


        public String getSchedulerName() {
            return schedulerName;
        }

        public List<String> getExecutionOrder() {
            return executionOrder;
        }

        public Map<String, Integer> getWaitingTimes() {
            return waitingTimes;
        }

        public Map<String, Integer> getTurnaroundTimes() {
            return turnaroundTimes;
        }

        public double getAvgWaitingTime() {
            return avgWaitingTime;
        }

        public double getAvgTurnaroundTime() {
            return avgTurnaroundTime;
        }


        public void printResults() {
            System.out.println();
            System.out.println("org.example.Scheduler Name: " + schedulerName);
            System.out.println();


            System.out.println("Execution Order:");
            for (int i = 0; i < executionOrder.size(); i++) {
                System.out.print(executionOrder.get(i));
                if (i < executionOrder.size() - 1) {
                    System.out.print(" -> ");
                }
            }
            System.out.println();
            System.out.println();


            System.out.println("Waiting Times:");
            for (String processName : waitingTimes.keySet()) {
                System.out.println(processName + ":" + waitingTimes.get(processName));
            }
            System.out.println();


            System.out.println("Turnaround Times:");
            for (String processName : turnaroundTimes.keySet()) {
                System.out.println(processName + ":" + turnaroundTimes.get(processName));
            }
            System.out.println();


            System.out.println("Average Waiting Time:" + avgWaitingTime);
            System.out.println("Average Turnaround Time:" + avgTurnaroundTime);


            if (quantumHistory.size() > 0) {
                System.out.println();
                System.out.println("Quantum History:");
                for (int i = 0; i < quantumHistory.size(); i++) {
                    System.out.println(quantumHistory.get(i));
                }
            }

            System.out.println();
        }
    }
