import java.util.Random;

enum ProcessState{
    READY, BLOCKED, SUSPENDED_READY, SUSPENDED_BLOCKED, FINISHED
}

public class SimProcess {
    int pid;
    String procName;
    int totalInstructions;

    public SimProcess(int id, String name, int total){
        pid = id;
        procName = name;
        totalInstructions = total;
    }

    public ProcessState execute(int i){
        System.out.println("Proc " + procName + ", PID: " + pid + " executing instruction: " + i);
        if(i >= totalInstructions){
            return ProcessState.FINISHED;
        }
        Random rand = new Random();
        int num = rand.nextInt(100);
        if(num < 15){
            return ProcessState.BLOCKED;
        }
        return ProcessState.READY;
    }

}
