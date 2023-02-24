/*
    420-201 – Environnements
    Date : 2022-11-07
    Groupe : 2 – lundi & jeudi
    Nom : Piche
    Prénom : Zakari
    DA : 2241035
*/

package separateloader;

import structures.JVMClosure;

public class Environnements {
    private JVMClosure[] envList;
    private int counter = 0;

    public Environnements() {
        envList = new JVMClosure[2];
    }

    public boolean isPresent(String str) {
        boolean res = false;
        for (int i = 0; i < envList.length && !res; i++) {
            if (envList[i].getName().equals(str))
                res = true;
        }
        return res;
    }

    public void addInClosures(JVMClosure cl) {
        envList[counter++] = cl;
    }

    public JVMClosure getClosure(String name) {
        for (int i = 0; i < envList.length; i++) {
            if (envList[i].getName().equals(name))
                return envList[i];
        }
        return null;
    }
}
