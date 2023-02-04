/*
    420-201 – Optimiser
    Date : 2022-11-07
    Groupe : 2 – lundi & jeudi
    Nom : Piche
    Prénom : Zakari
    DA : 2241035
*/
package Projets.yaboi_compiler;

import Projets.yaboi_compiler.Misc.Closure;
import Projets.yaboi_compiler.Misc.FuncBody;
import Projets.yaboi_compiler.Misc.LocVar;
import Projets.yaboi_compiler.Utilities.Utilities;

public class Optimiser {
    private Parser ps;

    public void setPs(Parser ps) {
        this.ps = ps;
    }

    public Optimiser(Parser ps) {
        setPs(ps);
    }

    public static LocVar searchVars(Object[] obj, String name, int stackPos) {
        LocVar res;

        for (int i = stackPos; i >= 0; i--) {
            if (obj[i].getClass() == LocVar.class) {
                res = ((LocVar) obj[i]);

                if (Utilities.isExactString(res.getName(), name)) {
                    return res;
                }
            }
        }
        return null;
    }

    public static Closure searchClosure(Object[] obj, String name, int stackPos) {
        Closure res;

        for (int i = stackPos; i >= 0; i--) {
            if (obj[i].getClass() == Closure.class) {
                res = ((Closure) obj[i]);

                if (Utilities.isExactString(res.getName(), name)) {
                    return res;
                }
            }
        }
        return null;
    }

    private boolean isVarGet(String name) {
        return (name.charAt(0) != '.') ? true : false; // check if string or other var type
    }

    public void checkVarsMutator() {
        Object[] temp = ps.getStackCl().getRawStack();

        for (int i = 0; i < temp.length; i++) {

            if (temp[i].getClass() == FuncBody.class) {
                Object[] funcParams = ((FuncBody) temp[i]).getParams();

                for (int j = 0; j < funcParams.length; j++) {

                    LocVar tmp = searchVars(temp, funcParams[j].toString(), i);

                    if (isVarGet(funcParams[j].toString().trim())) {
                        funcParams[j] = (tmp != null) ? tmp.getValue() : null;
                    }
                }
            }
        }
    }
}
