/*
    420-201 – Environnements
    Date : 2022-11-07
    Groupe : 2 – lundi & jeudi
    Nom : Piche
    Prénom : Zakari
    DA : 2241035
*/

package Projets.yaboi_compiler.SeparateLoader;

import Projets.yaboi_compiler.Misc.ThrClosure;

public class Environnements {
    private ThrClosure[] envList;
    public Environnements() {
        envList = new ThrClosure[1];
    }

    public boolean isPresent(String str) {
        boolean res = false;
        for (int i = 0; i < envList.length && !res; i++) {
            if (envList[i].getName().equals(str))
                res = true;
        }
        return res;
    }

    public void addInClosures(ThrClosure cl) {
        envList[0] = cl;
    }

    public ThrClosure getClosure(String name) {
        for (int i = 0; i < envList.length; i++) {
            if (envList[i].getName().equals(name))
                return envList[i];
        }
        return null;
    }
}
