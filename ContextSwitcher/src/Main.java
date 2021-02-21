import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Main {
    final int QUANTUM = 5;
    final int TOTAL_STEPS = 3_000;
    int instructNum = 0;
    SimProcessor simProcessor;
    Queue<ProcessControlBlock> readyProcesses = new LinkedList<>();
    LinkedList<ProcessControlBlock> blockedProcesses = new LinkedList<>();
    ProcessState status = null;

    public Main(){
        Random rand = new Random();
        for(int i = 0; i < 10; i++){
            int num = rand.nextInt(300) + 100;
            SimProcess sp = new SimProcess(i, "Process" + i, num);
            ProcessControlBlock pcb = new ProcessControlBlock(sp);
            readyProcesses.add(pcb);
        }
    }

    public void saveProcess(ProcessControlBlock currPcb){
        ArrayList<Integer> registerValues = simProcessor.getRegisterValues();
        for (int j = 0; j < registerValues.size(); j++) {
            currPcb.setRegisterValues(j, registerValues.get(j));
        }
        currPcb.setCurrInstruction(simProcessor.getCurrInstruction());
        System.out.println("Context switch: Saving process: " + currPcb.getSimProcess().pid);
        System.out.print("\tInstruction: " + currPcb.getCurrInstruction());
        for(int i = 0; i < currPcb.getRegisterValues().size(); i++){
            System.out.print(" - R" + (i+1) + ": " + currPcb.getRegisterValues().get(i));
        }
        System.out.println();
        if(status == ProcessState.READY) {
            readyProcesses.add(currPcb);
        }
    }

    public ProcessControlBlock restoreProcess(){
        if(readyProcesses.size() > 0) {
            ProcessControlBlock currPcb = readyProcesses.poll();
            simProcessor.setCurrProc(currPcb.getSimProcess());
            simProcessor.setCurrInstruction(currPcb.getCurrInstruction());
            System.out.println("Context switch: Restoring process: " + currPcb.getSimProcess().pid);
            System.out.print("\tInstruction: " + currPcb.getCurrInstruction());
            for (int i = 0; i < currPcb.getRegisterValues().size(); i++) {
                System.out.print(" - R" + (i + 1) + ": " + currPcb.getRegisterValues().get(i));
            }
            System.out.println();
            return currPcb;
        }
        System.out.println("No ready processes");
        return null;
    }

    public void processTasks(){
        simProcessor = new SimProcessor();
        ProcessControlBlock currPcb = readyProcesses.poll();
        boolean save = false;
        boolean restore = false;
        for(int i = 0; i < TOTAL_STEPS; i++){
            if(instructNum > 0 && instructNum % QUANTUM == 0 && !restore){
                System.out.println("*** Quantum expired ***");
                save = true;
            }
            System.out.print("Step " + i + " ");
            if(currPcb == null){
                currPcb = restoreProcess();
            }
            else if(save){
                saveProcess(currPcb);
                instructNum++;
                save = false;
                restore = true;
            }
            else if (restore){
                currPcb = restoreProcess();
                instructNum = 0;
                restore = false;
            }
            else {
                simProcessor.setCurrProc(currPcb.getSimProcess());
                status = simProcessor.executeNextInstruction();
                if(status == ProcessState.FINISHED){
                    System.out.println("*** Process Complete ***");
                    restore = true;
                }
                else if(status == ProcessState.BLOCKED){
                    System.out.println("*** Process Blocked ***");
                    blockedProcesses.add(currPcb);
                    save = true;
                }
                instructNum++;
            }
            Random rand = new Random();
            for(int j = blockedProcesses.size() - 1; j >= 0; j--){
                int num = rand.nextInt(100);
                ProcessControlBlock pcb = blockedProcesses.get(j);
                if(num < 30){
                    System.out.println("*** " + pcb.getSimProcess().procName + " Ready ***");
                    readyProcesses.add(pcb);
                    blockedProcesses.remove(pcb);
                }
            }
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.processTasks();
    }

}
