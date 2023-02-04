package Projets.yaboi_compiler.SeparateLoader;

/*
* THIS IS VERY TEMPORARY JUST TO TEST THE PARSER AND STACK OF A SCRIPT
* COPYRIGHT (C) ZP 2023
*/

import Projets.yaboi_compiler.Misc.Closure;
import Projets.yaboi_compiler.Misc.FuncBody;
import Projets.yaboi_compiler.Misc.LocVar;
import Projets.yaboi_compiler.Optimiser;
import Projets.yaboi_compiler.Parser;

public class Execution {
    private Parser ps;

    public Execution(Parser ps) {
    }

    public void runStackScript(Object[] rawSt, Environnements nvr) {
        for (int i = 0; i < rawSt.length; i++) {

            if (rawSt[i].getClass() == FuncBody.class) {
                FuncBody funcB = ((FuncBody) rawSt[i]);

                switch (funcB.getNmFct()) {
                    default: {
                        Closure cls = Optimiser.searchClosure(rawSt, funcB.getNmFct(), 0);
                        if (cls != null) {
                            Parser ps = new Parser(null, nvr);
                            ps.setCodeLines(cls.getScb());

                            for (int j = 0; j < cls.getPms().length; j++) {
                                ps.getStackCl().addElement(new LocVar("gbl", cls.getPms()[j], funcB.getParams()[j]));
                            }

                            ps.parseScript();
                            new Optimiser(ps).checkVarsMutator();

                            runStackScript(ps.getStackCl().getRawStack(), nvr);
                        } else if (nvr.isPresent(funcB.getNmFct())) {
                            //call them with thread but now, we need to found parameters better options...
                            String[] nil = {"well", "ji"}; //WORKING NEED TO WORK ON PARAMS
                            nvr.getClosure(funcB.getNmFct()).runJob(nil);
                        } else {
                            System.out.printf("Unknow: %s\n", funcB.getNmFct());
                        }
                        break;
                    }
                }
            }
        }
    }

    private void runVM() {

    }
}
