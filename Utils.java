package scheduler;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static int calculateWaitingTime(int turnaroundTime, int burstTime) {
        return turnaroundTime - burstTime;
    }


    public static int calculateTurnaroundTime(int completionTime, int arrivalTime) {
        return completionTime - arrivalTime;
    }


    public static double calculateAverage(List<Integer> numbers) {
        if (numbers.size() == 0) {
            return 0;
        }

        int sum = 0;
        for (int i = 0; i < numbers.size(); i++) {
            sum = sum + numbers.get(i);
        }

        double average = (double) sum/numbers.size();
        return average;
    }


    public static int calculate25Percent(int quantum) {
        double result = quantum * 0.25;
        return (int) Math.ceil(result);
    }


    public static int calculate50Percent(int quantum) {
        double result = quantum * 0.50;
        return (int) Math.ceil(result);
    }


    public static void sortByRemainingTime(List<Process> processes) {
        for (int i = 0; i < processes.size() - 1; i++) {
            for (int j = 0; j < processes.size() - i - 1; j++) {
                if (processes.get(j).getRemainingTime() > processes.get(j + 1).getRemainingTime()) {
                    // swap
                    Process temp = processes.get(j);
                    processes.set(j, processes.get(j + 1));
                    processes.set(j + 1, temp);
                }
            }
        }
    }


    public static void sortByPriority(List<Process> processes) {
        for (int i = 0; i < processes.size() - 1; i++) {
            for (int j = 0; j < processes.size() - i - 1; j++) {
                if (processes.get(j).getPriority() > processes.get(j + 1).getPriority()) {

                    Process temp = processes.get(j);
                    processes.set(j, processes.get(j + 1));
                    processes.set(j + 1, temp);
                }
            }
        }
    }


    public static void sortByArrivalTime(List<Process> processes) {
        for (int i = 0; i < processes.size() - 1; i++) {
            for (int j = 0; j < processes.size() - i - 1; j++) {
                if (processes.get(j).getArrivalTime() > processes.get(j + 1).getArrivalTime()) {

                    Process temp = processes.get(j);
                    processes.set(j, processes.get(j + 1));
                    processes.set(j + 1, temp);
                }
            }
        }
    }


    public static Process getShortestJob(List<Process> processes) {
        if (processes.size() == 0) {
            return null;
        }

        Process shortest = processes.get(0);
        for (int i = 1; i < processes.size(); i++) {
            if (processes.get(i).getRemainingTime() < shortest.getRemainingTime()) {
                shortest = processes.get(i);
            }
        }
        return shortest;
    }


    public static Process getHighestPriority(List<Process> processes) {
        if (processes.size() == 0) {
            return null;
        }

        Process highest = processes.get(0);
        for (int i = 1; i < processes.size(); i++) {
            if (processes.get(i).getPriority() < highest.getPriority()) {
                highest = processes.get(i);
            }
        }
        return highest;
    }


    public static List<Process> getArrivedProcesses(List<Process> allProcesses, int currentTime) {
        List<Process> arrived = new ArrayList<>();
        for (int i = 0; i < allProcesses.size(); i++) {
            Process p = allProcesses.get(i);
            if (p.getArrivalTime() <= currentTime && !p.isComplete()) {
                arrived.add(p);
            }
        }
        return arrived;
    }


    public static double calculateAGFactor(Process p) {
        double factor = (p.getPriority() + p.getArrivalTime() + p.getBurstTime()) / 3.0;
        return factor;
    }


    public static void sortByAGFactor(List<Process> processes) {
        for (int i = 0; i < processes.size() - 1; i++) {
            for (int j = 0; j < processes.size() - i - 1; j++) {
                double factor1 = calculateAGFactor(processes.get(j));
                double factor2 = calculateAGFactor(processes.get(j + 1));
                if (factor1 > factor2) {

                    Process temp = processes.get(j);
                    processes.set(j, processes.get(j + 1));
                    processes.set(j + 1, temp);
                }
            }
        }
    }


    public static boolean allComplete(List<Process> processes) {
        for (int i = 0; i < processes.size(); i++) {
            if (!processes.get(i).isComplete()) {
                return false;
            }
        }
        return true;
    }


    public static List<Process> copyProcessList(List<Process> processes) {
        List<Process> copy = new ArrayList<>();
        for (int i = 0; i < processes.size(); i++) {
            copy.add(processes.get(i).copy());
        }
        return copy;
    }
}
