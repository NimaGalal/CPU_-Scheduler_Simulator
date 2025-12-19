package scheduler;

import java.util.*;

public class RoundRobinScheduler {

    public static SchedulerResult schedule(List<Process> processes, int quantum, int contextSwitch) {
        SchedulerResult result = new SchedulerResult("Round Robin");

        List<Process> processList = Utils.copyProcessList(processes);
        Utils.sortByArrivalTime(processList);

        // Scheduling structures
        Queue<Process> readyQueue = new LinkedList<>();
        int currentTime = 0, completed = 0, total = processList.size();
        int nextProcessIndex = 0; // To track processes that not yet in ready queue

        // Initial processes
        while (nextProcessIndex < total && 
                processList.get(nextProcessIndex).getArrivalTime() <= currentTime) {
            readyQueue.add(processList.get(nextProcessIndex++));
        }

        // Main scheduling loop
        while (completed < total) {
            if (readyQueue.isEmpty()) {
                // Jump to next arrival if queue is empty
                if (nextProcessIndex < total) {
                    currentTime = processList.get(nextProcessIndex).getArrivalTime();
                    while (nextProcessIndex < total && 
                            processList.get(nextProcessIndex).getArrivalTime() <= currentTime) {
                        readyQueue.add(processList.get(nextProcessIndex++));
                    }
                }
                continue;
            }

            // Get and execute the next process
            Process current = readyQueue.poll();
            result.addToExecutionOrder(current.getName());

            int execTime = Math.min(quantum, current.getRemainingTime());
            current.execute(execTime);
            currentTime += execTime;

            // Add the newly arrived processes
            while (nextProcessIndex < total && 
                    processList.get(nextProcessIndex).getArrivalTime() <= currentTime) {
                readyQueue.add(processList.get(nextProcessIndex++));
            }

            // Handle completed process
            if (current.isComplete()) {
                completed++;
                current.setCompletionTime(currentTime);
                int turnaround = current.getCompletionTime() - current.getArrivalTime();
                int waiting = turnaround - current.getBurstTime();
                result.addProcessTimes(current.getName(), waiting, turnaround);
            } else {
                readyQueue.add(current); // Return to queue if not finished
            }

            // Context switching overhead
            if (completed < total && contextSwitch > 0) {
                currentTime += contextSwitch;
                while (nextProcessIndex < total && 
                        processList.get(nextProcessIndex).getArrivalTime() <= currentTime) {
                    readyQueue.add(processList.get(nextProcessIndex++));
                }
            }
        }

        result.calculateAverages();
        return result;
    }
}