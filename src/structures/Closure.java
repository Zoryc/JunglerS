package structures;

public class Closure {
    private String[] scb;
    private String[] pms;
    private String name;
    private int posStack;

    public String getName() {
        return name;
    }

    public String[] getPms() {
        return pms;
    }

    public String[] getScb() {
        return scb;
    }

    public void setScb(String[] scb) {
        this.scb = scb;
    }

    public Closure(String[] blcs, String name, String[] pms, int pos) {
        setScb(blcs);
        posStack = pos;
        this.name = name;
        this.pms = pms;
    }

    public void showDebug() {
        System.out.println("CLOSURE: name: " + name);
    }
}
