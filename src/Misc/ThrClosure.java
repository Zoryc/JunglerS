package Projets.yaboi_compiler.Misc;

public class ThrClosure {
    private Runnable clExec;
    private String name;
    private String[][] stack;

    public String getName() {
        return name;
    }

    public String[][] getStack() {
        return stack;
    }

    public ThrClosure(Runnable yes, String name, String[][] stc) {
        clExec = yes;
        this.name = name;
        stack = stc;
    }

    public void runJob(String[] arr) { // run singleThread for now...
        stack[0] = arr;
        clExec.run();
    }

}
