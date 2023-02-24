package separateloader;

/*
* THIS IS VERY TEMPORARY JUST TO TEST THE PARSER AND STACK OF A SCRIPT
* COPYRIGHT (C) ZP 2023
*/

import lexique.GlobalTypes;
import structures.*;
import tools.StringArray;
import zothers.ExpressionParser;
import zothers.Optimiser;


public class Execution {
    private ExpressionParser pse;
    private LocVar ret;

    public Execution(ExpressionParser pse) {
        this.pse = pse;
    }

    public LocVar getReturnValue() {
        return ret;
    }

    public void runStackScript(Object[] rawSt, Environnements nvr) {
        for (int i = 0; i < rawSt.length; i++) {

            if (rawSt[i].getClass() == FuncBody.class) {
                FuncBody funcB = ((FuncBody) rawSt[i]);

                Closure cls = Optimiser.searchClosure(rawSt, funcB.getNmFct(), 0);
                if (cls != null) {
                    ExpressionParser ps = new ExpressionParser(null, nvr);

                    StringArray arrs = new StringArray(cls.getScb()); // VERY TEMPORARY
                    ps.setCodeLines(arrs);

                    for (int j = 0; j < funcB.getParams().length; j++) {
                        ps.getStackCl().addElement(new LocVar(GlobalTypes.getGlobalVar(funcB.getParams()[j].getName()), cls.getPms()[j], funcB.getParams()[j].getName()));
                    }

                    ps.parseScript();
                    new Optimiser(ps, nvr).checkVarsMutator();

                    runStackScript(ps.getStackCl().getRawStack(), nvr);
                } else if (nvr.isPresent(funcB.getNmFct())) {
                    //call them with thread but now, we need to found parameters better options...
                    int numParams = funcB.getParams().length;

                    LocVar[] params = new LocVar[numParams]; // WORKING NEED TO WORK ON PARAMS
                    for (int j = 0; j < numParams; j++) {
                        LocVar curr = funcB.getParams()[j];
                        params[j] = curr;
                    }

                    nvr.getClosure(funcB.getNmFct()).runJob(params);
                } else {
                    System.out.printf("Unknow: %s\n", funcB.getNmFct());
                }
            } else if (rawSt[i].getClass() == LocVar.class) {
                LocVar currentVar = ((LocVar) rawSt[i]);

                String val = new String();

                for (int j = 0; j < ((LocVar) rawSt[i]).getValue().toString().length(); j++) {
                    if (((LocVar) rawSt[i]).getValue().toString().charAt(j) == '(')
                        break;
                    val += ((LocVar) rawSt[i]).getValue().toString().charAt(j);
                }

                LocVar[] params = new LocVar[0]; // WORKING NEED TO WORK ON PARAMS

                if (nvr.isPresent(val)) {
                    JVMClosure yes = nvr.getClosure(val);

                    if (yes != null) {
                        yes.runJob(params);

                        LocVar returnValue = yes.getReturnVal(); // if theres one...

                        if (returnValue != null) {
                            rawSt[i] = new LocVar(returnValue.getType(), currentVar.getName(), returnValue.getValue());
                            new Optimiser(pse, nvr).checkVarsMutator(); // Do it after changing variable
                        }
                    }
                } else if (Optimiser.searchClosure(rawSt, val, i) != null) {
                    ExpressionParser ps = new ExpressionParser(null, nvr);

                    StringArray arrs = new StringArray(Optimiser.searchClosure(rawSt, val, i).getScb()); // VERY TEMPORARY
                    ps.setCodeLines(arrs);

                    ps.parseScript();
                    new Optimiser(ps, nvr).checkVarsMutator();

                    Execution yet = new Execution(ps);
                    yet.runStackScript(ps.getStackCl().getRawStack(), nvr);

                    LocVar retLoc = yet.ret;

                    rawSt[i] = new LocVar(retLoc.getType(), currentVar.getName(), retLoc.getValue());
                    new Optimiser(pse, nvr).checkVarsMutator(); // Do it after changing variable
                }
            } else if (rawSt[i].getClass() == ReturnVal.class) {
                ret = ((ReturnVal) rawSt[i]).getRet();
            }
        }
    }
}
