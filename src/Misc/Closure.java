package Projets.yaboi_compiler.Misc;

public class Closure {
    private String[] scb;
    private String[] pms;
    private String name;
    private int posStack;
    private boolean isInternal;

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
        isInternal = (scb.length == 0) ? true : false;
        this.scb = scb;
    }

    public Closure(String[] blcs, String name, String[] pms, int pos) {
        setScb(blcs);
        posStack = pos;
        this.name = name;
        this.pms = pms;
    }

    public void showDebug() {

    }
}
