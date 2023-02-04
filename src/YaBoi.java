package Projets.yaboi_compiler;

/*
    420-201 – YaBoi
    Date : 2022-11-03
    Groupe : 2 – lundi & jeudi
    Nom : Piche
    Prénom : Zakari
    DA : 2241035
*/

import Projets.yaboi_compiler.Misc.ThrClosure;
import Projets.yaboi_compiler.SeparateLoader.Environnements;
import Projets.yaboi_compiler.SeparateLoader.Execution;
import Projets.yaboi_compiler.Utilities.UserInput;
import Projets.yaboi_compiler.Utilities.Utilities;

public class YaBoi {
    private String[][] stackcl;
    public YaBoi() {
        Environnements mainEnv = new Environnements();
        stackcl = new String[1][0]; // share the address
        Runnable yes = new Runnable() {
            @Override
            public void run() {
                printFunc(stackcl);
            }
        };

        mainEnv.addInClosures(new ThrClosure(yes, "print", stackcl));

        String scr = new String();
        Parser ps = new Parser(null, mainEnv);

        scr += UserInput.getUserText();

        ps.postProcessing(scr);
        if (ps.parseScript()) {
            System.out.println(ps.getErrorMessage());
            ps.showDebugResults();
        } else {
            ps.showDebugResults();

            new Optimiser(ps).checkVarsMutator(); // Optimiser for vars and remove inutilised vars or func...

            // Code generation to a VM coded in cpp or a local  stack script execution without passing in VM
            System.out.println(" - Script Output BEGIN - ");
            new Execution(ps).runStackScript(ps.getStackCl().getRawStack(), mainEnv); // then VM runs (optional) with Thread environnements and BOOM... magic happen
            System.out.println(" - Script Output END - ");
        }
    }

    public void printFunc(String[][] num) {
        System.out.println("hgfhfgh");
        Utilities.afficherTableau(num[0]);
    }

    public static void main(String[] args) {
        new YaBoi();
    }
}
