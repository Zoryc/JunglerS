package structures;

public class JVMClosure {
    private Runnable clExec;
    private String name;
    private LocVar[][] stack;

    private LocVar returnVal;

    public String getName() {
        return name;
    }

    public LocVar[][] getStack() {
        return stack;
    }

    public JVMClosure(Runnable yes, String name, LocVar[][] stc) {
        clExec = yes;
        this.name = name;
        stack = stc;
    }

    public LocVar getReturnVal() {
        return returnVal;
    }

    public void runJob(LocVar[] arr) { // run singleThread for now...
        stack[0] = arr;
        clExec.run();
        returnVal = (stack[0] != arr) ? stack[0][0] : null;
    }

}
