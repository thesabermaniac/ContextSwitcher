import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class SimProcessor {
    private SimProcess currProc;
    private int reg1, reg2, reg3, reg4;
    private ArrayList<Integer> registerValues = new ArrayList<>(Arrays.asList(reg1, reg2, reg3, reg4));
    private int currInstruction;

    public SimProcess getCurrProc(){
        return currProc;
    }

    public void setCurrProc(SimProcess process){
        currProc = process;
    }

    public ArrayList<Integer> getRegisterValues(){
        return registerValues;
    }

    public void setRegisterValues(int index, int regVal){
        registerValues.set(index, regVal);
    }

    public int getCurrInstruction(){
        return currInstruction;
    }

    public void setCurrInstruction(int currInstruction) {
        this.currInstruction = currInstruction;
    }

    public ProcessState executeNextInstruction(){
        ProcessState procStatus = currProc.execute(currInstruction);
        currInstruction++;
        Random rand = new Random();
        for(int i = 0; i < registerValues.size(); i++){
            setRegisterValues(i, rand.nextInt(Integer.MAX_VALUE));
        }
        return procStatus;
    }
}
