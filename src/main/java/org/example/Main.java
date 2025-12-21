package org.example;
import java.io.IOException;
import java.nio.file.*;
import org.json.*;

import java.util.*;

import static java.lang.Math.max;


public class Main {
    public static class Process {

        private String name;
        private int arrivalTime;
        private int burstTime;
        private int priority;
        private int quantum;


        private int remainingTime;
        private int waitingTime;
        private int turnaroundTime;
        private int completionTime;


        public Process(String name, int arrivalTime, int burstTime, int priority, int quantum) {
            this.name = name;
            this.arrivalTime = arrivalTime;
            this.burstTime = burstTime;
            this.priority = priority;
            this.quantum = quantum;
            this.remainingTime = burstTime;
            this.waitingTime = 0;
            this.turnaroundTime = 0;
            this.completionTime = 0;
        }


        public String getName() {
            return name;
        }

        public int getArrivalTime() {
            return arrivalTime;
        }

        public int getBurstTime() {
            return burstTime;
        }

        public int getRemainingTime() {
            return remainingTime;
        }

        public int getPriority() {
            return priority;
        }

        public int getQuantum() {
            return quantum;
        }

        public int getWaitingTime() {
            return waitingTime;
        }

        public int getTurnaroundTime() {
            return turnaroundTime;
        }

        public int getCompletionTime() {
            return completionTime;
        }


        public void setRemainingTime(int remainingTime) {
            this.remainingTime = remainingTime;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }

        public void setQuantum(int quantum) {
            this.quantum = quantum;
        }

        public void setWaitingTime(int waitingTime) {
            this.waitingTime = waitingTime;
        }

        public void setTurnaroundTime(int turnaroundTime) {
            this.turnaroundTime = turnaroundTime;
        }

        public void setCompletionTime(int completionTime) {
            this.completionTime = completionTime;
        }


        public boolean isComplete() {
            if (remainingTime <= 0) {
                return true;
            }
            return false;
        }


        public void execute(int time) {
            if (time > remainingTime) {
                time = remainingTime;
            }
            remainingTime = remainingTime - time;
        }


        public Process copy() {
            Process newProcess = new Process(name, arrivalTime, burstTime, priority, quantum);
            newProcess.remainingTime = this.remainingTime;
            newProcess.waitingTime = this.waitingTime;
            newProcess.turnaroundTime = this.turnaroundTime;
            newProcess.completionTime = this.completionTime;
            return newProcess;
        }
    }


    public static class Scheduler {
        ArrayList<Main.Process> processes;
        int contextSwitch ;
        int rrQuantum ;
        int agingInterval ;
        int numberOfProcesses;
        int waitingTime = 0;

        Scheduler()
        {
            processes = new ArrayList<>();
        }

        void addProcess(Main.Process p) {
            processes.add(p);
            numberOfProcesses++;
        }

        private void setProcessesWaitingTime(int waitingTime) {
            for (int i = 0; i < numberOfProcesses; i++) {
                Main.Process tempProcess = processes.get(i);
                tempProcess.setWaitingTime(waitingTime);
            }
        }
        private void setContextSwitch(int contextSwitch) {
            this.contextSwitch = contextSwitch;
        }
        private void getContextSwitch(int contextSwitch) {
            this.contextSwitch = contextSwitch;
        }
        private void setRrQuantum(int rrQuantum) {
            this.rrQuantum = rrQuantum;
        }
        private void getRrQuantum(int rrQuantum) {
            this.rrQuantum = rrQuantum;
        }
        private void setAgingInterval(int agingInterval) {
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
    public static class SchedulerResult {

        private String schedulerName;
        private List<String> executionOrder;
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


    public class JsonToHashMap {

        public static HashMap<String, Object> toMap(JSONObject json) {
            HashMap<String, Object> map = new HashMap<>();

            for (String key : json.keySet()) {
                Object value = json.get(key);

                if (value instanceof JSONObject) {
                    map.put(key, toMap((JSONObject) value));
                }
                else if (value instanceof JSONArray) {
                    map.put(key, toList((JSONArray) value));
                }
                else {
                    map.put(key, value);
                }
            }
            return map;
        }

        private static List<Object> toList(JSONArray array) {
            List<Object> list = new ArrayList<>();

            for (int i = 0; i < array.length(); i++) {
                Object value = array.get(i);

                if (value instanceof JSONObject) {
                    list.add(toMap((JSONObject) value));
                }
                else if (value instanceof JSONArray) {
                    list.add(toList((JSONArray) value));
                }
                else {
                    list.add(value);
                }
            }
            return list;
        }
    }

    public static class JSONparser {
        Scheduler scheduler;
        JSONparser(String FilePath) throws IOException {
            String jsonText = Files.readString(Path.of(FilePath));

            JSONObject jsonObject = new JSONObject(jsonText);
            JSONObject input = jsonObject.getJSONObject("input");
            JSONArray processes = input.getJSONArray("processes") ;

            HashMap<String, Object> data = JsonToHashMap.toMap(jsonObject);
            this.scheduler = new Scheduler();
            if(input.has("rrQuantum")) {
                this.scheduler.setRrQuantum(input.getInt("rrQuantum"));
            }
            if(input.has("agingInterval")) {
                this.scheduler.setAgingInterval(input.getInt("agingInterval"));
            }
            if(input.has("contextSwitch")) {
                this.scheduler.setContextSwitch(input.getInt("contextSwitch"));
            }
            for (int i = 0 ; i < processes.length() ; i++) {
                JSONObject process  = (JSONObject) processes.get(i);
                int arrivalTime = 0 ;
                if(process.has("arrival"))
                {
                    arrivalTime = process.getInt("arrival");
                }
                int burstTime = 0 ;
                if(process.has("burst")){
                    burstTime = process.getInt("burst");
                }
                int priority  = 0 ;
                if(process.has("priority")){
                    priority = process.getInt("priority");
                }
                String name = " ";
                if(process.has("name") ){
                    name = (String) process.getString("name");
                }
                int quantum  = 0 ;
                if(process.has("quantum")){
                    quantum = process.getInt("quantum");
                }
                Process tempProcess = new Process(name, arrivalTime, burstTime, priority, quantum);
                this.scheduler.addProcess(tempProcess);
            }
        }
        public Scheduler getScheduler() {
            return scheduler;
        }
    }


    static void main() throws IOException {
        // String pwd = System.getProperty("user.dir");
        // System.out.println(pwd);
        for(int i = 1 ; i <= 6 ; i++) {
            System.out.println("----------------- Test Case " + i + " -----------------");
            JSONparser TestCase = new JSONparser("src\\\\test\\\\Other_Schedulers\\\\test_" + i + ".json");
            Scheduler scheduler = TestCase.getScheduler() ;
            SchedulerResult result = scheduler.runPriority();
            result.printResults();
            System.out.println("-------------------------------------------------");
            System.out.println("-------------------------------------------------");
        }
    }
}
