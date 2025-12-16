package org.example;
import java.io.IOException;
import java.nio.file.*;
import org.json.*;
import org.json.JSONObject;
import java.util.*;


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
            ArrayList<Process> readyList = new ArrayList<>();
            for (Process p : processes) {
                readyList.add(p.copy());
            }

            int currentTime = 0;
            int completed = 0;
            int n = readyList.size();

            while (completed < n) {

                Process highestPriority = null;

                for (Process p : readyList) {
                    if (p.getArrivalTime() <= currentTime && !p.isComplete()) {
                        if (highestPriority == null ||
                                p.getPriority() < highestPriority.getPriority()) {
                            highestPriority = p;
                        }
                    }
                }

                if (highestPriority == null) {
                    currentTime++;
                    continue;
                }

                highestPriority.execute(1);
                result.addToExecutionOrder(highestPriority.getName());
                currentTime++;

                if (highestPriority.isComplete()) {
                    highestPriority.setCompletionTime(currentTime);

                    int tat = currentTime - highestPriority.getArrivalTime();
                    int wt = tat - highestPriority.getBurstTime();

                    highestPriority.setTurnaroundTime(tat);
                    highestPriority.setWaitingTime(wt);

                    result.addProcessTimes(
                            highestPriority.getName(),
                            wt,
                            tat
                    );

                    completed++;
                }
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
        JSONparser TestCase = new JSONparser("c:\\\\Users\\\\Omar Awad\\\\Downloads\\\\Compressed\\\\test_cases_updated\\\\test_cases\\\\Other_Schedulers\\\\test_2.json") ;
        Scheduler scheduler = TestCase.getScheduler() ;
        SchedulerResult result = scheduler.runPriority();
        result.printResults();
    }
}
