package zothers;

/*
    420-201 – Others.Optimiser
    Date : 2022-11-07
    Groupe : 2 – lundi & jeudi
    Nom : Piche
    Prénom : Zakari
    DA : 2241035
*/


import lexique.GlobalTypes;
import lexique.Operations;
import separateloader.Environnements;
import structures.Closure;
import structures.FuncBody;
import structures.LocVar;
import structures.ReturnVal;
import tools.Multi;

public class Optimiser {
    private ExpressionParser ps;
    private Environnements env;

    public Optimiser(ExpressionParser ps, Environnements env) {
        this.ps = ps;
        this.env = env;
    }

    public static LocVar searchVars(Object[] obj, String name, int stackPos) {
        LocVar res;

        for (int i = stackPos; i >= 0; i--) {
            if (obj[i].getClass() == LocVar.class) {
                res = ((LocVar) obj[i]);

                if (Multi.isExactString(res.getName(), name)) {
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

                if (Multi.isExactString(res.getName(), name)) {
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
                LocVar[] funcParams = ((FuncBody) temp[i]).getParams();

                for (int j = 0; j < funcParams.length; j++) {

                    LocVar tmp = searchVars(temp, funcParams[j].getName(), i);

                    if (isVarGet(funcParams[j].getName()) && tmp != null && tmp.getType() != GlobalTypes.gType.unknow && tmp.getType() != GlobalTypes.gType.closure) {
                        funcParams[j] = tmp;
                    } else if (!isVarGet(funcParams[j].getName())) { // repair string fix...
                        LocVar newTemp = new LocVar(GlobalTypes.getGlobalVar(funcParams[j].getName()), funcParams[j].getName(), funcParams[j].getName());
                        funcParams[j] = newTemp;
                    }
                }
            } else if (temp[i].getClass() == LocVar.class) {
                LocVar vrs = (LocVar) temp[i];

                LocVar tmp = searchVars(temp, vrs.getValue().toString(), i);

                if (tmp != null) {
                    vrs.setValue(tmp.getValue());
                } else if (vrs.getType() == GlobalTypes.gType.table) {
                    vrs.setValue(ps.parseArray(vrs.getValue().toString()));
                }
            } else if (temp[i].getClass() == ReturnVal.class) {
                ReturnVal tmp = (ReturnVal) temp[i];

                String locVal = tmp.getRet().getValue().toString();

                if (!isVarGet(locVal)) {
                    tmp.setRet(searchVars(temp, tmp.getRet().getValue().toString(), i)); // Return is stored in value...
                }
            }
        }
    }

    public void checkOperations() {
        Object[] temp = ps.getStackCl().getRawStack();

        for (int i = 0; i < temp.length; i++) {

            if (temp[i].getClass() == LocVar.class) {

                LocVar vrs = (LocVar) temp[i];
                if (Operations.isOperation(vrs.getValue().toString()) == true) {
                    System.out.println(ps.parseOperation(vrs.getValue().toString()));
                }

            }
        }
    }
}
