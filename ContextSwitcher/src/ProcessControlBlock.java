import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class ProcessControlBlock {
    private SimProcess simProcess;
    private int currInstruction;
    private int reg1, reg2, reg3, reg4;
    private ArrayList<Integer> registerValues = new ArrayList<>(Arrays.asList(reg1, reg2, reg3, reg4));

    public ProcessControlBlock(SimProcess process){
        simProcess = process;
        currInstruction = 0;
        Random rand = new Random();
        for(int i = 0; i < registerValues.size(); i++){
            setRegisterValues(i, rand.nextInt(Integer.MAX_VALUE));
        }
    }

    public ArrayList<Integer> getRegisterValues() {
        return registerValues;
    }

    public void setRegisterValues(int index, int regVal){
        registerValues.set(index, regVal);
    }

    public SimProcess getSimProcess() {
        return simProcess;
    }

    public int getCurrInstruction() {
        return currInstruction;
    }

    public void setCurrInstruction(int currInstruction) {
        this.currInstruction = currInstruction;
    }
}

