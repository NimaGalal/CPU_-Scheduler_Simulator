package org.example;

import java.util.*;


public class Scheduler {
        ArrayList<Process> processes;
        int contextSwitch ;
        int rrQuantum ;
        int agingInterval ;
        int numberOfProcesses;
        int waitingTime = 0;

        Scheduler()
        {
            processes = new ArrayList<>();
        }

        public Scheduler(List<Process> processes2, int contextSwitch2, int rrQuantum2, int agingInterval2) {
            this.processes = new ArrayList<>(processes2);
            this.contextSwitch = contextSwitch2;
            this.rrQuantum = rrQuantum2;
            this.agingInterval = agingInterval2;
            this.numberOfProcesses = processes2.size();
        }

        void addProcess(Process p) {
            processes.add(p);
            numberOfProcesses++;
        }

        private void setProcessesWaitingTime(int waitingTime) {
            for (int i = 0; i < numberOfProcesses; i++) {
                Process tempProcess = processes.get(i);
                tempProcess.setWaitingTime(waitingTime);
            }
        }
        void setContextSwitch(int contextSwitch) {
            this.contextSwitch = contextSwitch;
        }
        private void getContextSwitch(int contextSwitch) {
            this.contextSwitch = contextSwitch;
        }
        void setRrQuantum(int rrQuantum) {
            this.rrQuantum = rrQuantum;
        }
        private void getRrQuantum(int rrQuantum) {
            this.rrQuantum = rrQuantum;
        }
        void setAgingInterval(int agingInterval) {
            this.agingInterval = agingInterval;
        }
        private void getAgingInterval(int agingInterval) {
            this.agingInterval = agingInterval;
        }

    public SchedulerResult runPriority() {

        SchedulerResult result = new SchedulerResult("Priority Scheduling");

        List<Process> list = new ArrayList<>();
        for (Process p : processes) {
            list.add(p.copy());
        }

        int n = list.size();
        int completed = 0;
        int currentTime = 0;

        Process lastRunning = null;

        Map<Process, Integer> lastReadyTime = new HashMap<>();
        for (Process p : list) {
            lastReadyTime.put(p, p.getArrivalTime());
        }

        while (completed < n) {

            Process current = null;

            for (Process p : list) {
                if (p.getArrivalTime() <= currentTime && !p.isComplete()) {
                    if (current == null ||
                        p.getPriority() < current.getPriority() ||
                        (p.getPriority() == current.getPriority() &&
                        p.getArrivalTime() < current.getArrivalTime())) {
                        current = p;
                    }
                }
            }

            if (current == null) {
                currentTime++;
                lastRunning = null;
                continue;
            }

            if (lastRunning != null && lastRunning != current) {
                currentTime += contextSwitch;
            }

            current.execute(1);
            if (result.executionOrder.isEmpty() || !result.executionOrder.get(result.executionOrder.size()-1).equals(current.getName())) {
                result.addToExecutionOrder(current.getName());
            }
            currentTime++;

            
            for (Process p : list) {
                if (p != current &&
                    p.getArrivalTime() <= currentTime &&
                    !p.isComplete()) {

                    int waited = currentTime - lastReadyTime.get(p);

                    if (waited >= agingInterval) {
                        p.setPriority(Math.max(1, p.getPriority() - 1));
                        lastReadyTime.put(p, currentTime);
                    }
                }
            }
            if (current.isComplete()) {
                current.setCompletionTime(currentTime);

                int tat = currentTime - current.getArrivalTime();
                int wt = tat - current.getBurstTime();

                current.setTurnaroundTime(tat);
                current.setWaitingTime(wt);

                result.addProcessTimes(
                        current.getName(),
                        wt,
                        tat
                );

                completed++;
            }

            if (!current.isComplete()) {
                lastReadyTime.put(current, currentTime);
            }

            lastRunning = current;
        }

        result.calculateAverages();
        return result;
    }        
}
