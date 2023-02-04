/*
    420-201 – ScriptStack
    Date : 2022-11-10
    Groupe : 2 – lundi & jeudi
    Nom : Piche
    Prénom : Zakari
    DA : 2241035
*/

package Projets.yaboi_compiler;

import Projets.yaboi_compiler.Misc.Closure;
import Projets.yaboi_compiler.Misc.FuncBody;
import Projets.yaboi_compiler.Misc.LocVar;
import Projets.yaboi_compiler.Utilities.ArrayUtilities;

public class ScriptStack {
    private final static Object[] STACK_DEFAULT = {};
    private Object[] rawStack;
    private Object[] closureList;
    public Object[] getRawStack() {
        return rawStack;
    }

    public void setRawStack(Object[] stackTb) {
        this.rawStack = stackTb;
    }

    public ScriptStack() {
        this(STACK_DEFAULT);
    }

    public ScriptStack(Object[] stackTb) {
        setRawStack(stackTb);
    }

    public void addElement(Object ob) {
        int len = rawStack.length;

        if (len == 0) {
            rawStack = ArrayUtilities.createArray(1);
        } else {
            rawStack = ArrayUtilities.resizeArray(rawStack, len + 1);
        }

        rawStack[rawStack.length - 1] = ob; // newLen
    }

    public void remElement(Object ob) {
    }

    void showDebug() {
        System.out.println(" - Stack Classes - ");
        for (int i = 0; i < rawStack.length; i++) {
            System.out.printf("[%d] %s\n", i,rawStack[i].getClass().getName());
        }

        System.out.println(" - Stack Info - ");
        for (int i = 0; i < rawStack.length; i++) {
            if (rawStack[i] != null) {
                System.out.printf("[%d] ", i);

                if (rawStack[i].getClass() == FuncBody.class)
                    ((FuncBody) rawStack[i]).fctInfo();
                else if (rawStack[i].getClass() == LocVar.class) {
                    ((LocVar) rawStack[i]).showVars();
                } else if (rawStack[i].getClass() == Closure.class) {
                    ((Closure) rawStack[i]).showDebug();
                }
            }
        }
    }
}
