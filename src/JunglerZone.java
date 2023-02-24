/*
    420-201 – YaBoi
    Date : 2022-11-03
    Groupe : 2 – lundi & jeudi
    Nom : Piche
    Prénom : Zakari
    DA : 2241035
*/

import lexique.GlobalTypes;
import structures.JVMClosure;
import structures.LocVar;
import zothers.ExpressionParser;
import zothers.Optimiser;
import separateloader.Environnements;
import separateloader.Execution;
import tools.UserInput;

public class JunglerZone {
    private LocVar[][] stackcl; // share the address
    public JunglerZone() {
        Environnements mainEnv = new Environnements();
        stackcl = new LocVar[1][0];
        Runnable yes = new Runnable() {
            @Override
            public void run() {
                printFunc(stackcl[0]);
            }
        };

        Runnable yep = new Runnable() {
            @Override
            public void run() {
                mathFunc(stackcl);
            }
        };

        mainEnv.addInClosures(new JVMClosure(yes, "print", stackcl));
        mainEnv.addInClosures(new JVMClosure(yep, "testmath", stackcl));

        String scr = new String();
        ExpressionParser ps = new ExpressionParser(null, mainEnv);

        System.out.println("Please write the code below:");
        scr += UserInput.getUserText();

        ps.postProcessing(scr);
        if (ps.parseScript()) {
            System.out.println("[ERROR] " + ps.getErrorMessage());
            ps.showDebugResults();
        } else {
            ps.showDebugResults();

            Optimiser opt = new Optimiser(ps, mainEnv); // Others.Optimiser for vars and remove inutilised vars or func... and operation simplifier...
            opt.checkOperations();
            opt.checkVarsMutator();

            // Code generation to a VM coded in cpp or a local  stack script execution without passing in VM
            System.out.println(" - Script Output BEGIN - ");
            new Execution(ps).runStackScript(ps.getStackCl().getRawStack(), mainEnv); // then VM runs (optional) with Thread environnements and BOOM... magic happen
            System.out.println(" - Script Output END - ");
        }
    }

    public void printFunc(LocVar[] num) {
        for (int i = 0; i < num.length; i++) {
            if (num[i].getType() == GlobalTypes.gType.table)
                System.out.println("WORKING!!!");
            System.out.print(num[i].getValue().toString());
        }
        System.out.println();
    }

    public void mathFunc(LocVar[][] num) {
        num[0] = new LocVar[1];
        num[0][0] = new LocVar(GlobalTypes.gType.number, "well", "5");// RETURN
    }

    public static void main(String[] args) {
        new JunglerZone();
    }
}
