package scheduler;

public class Process {

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
