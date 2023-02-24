package structures;
/*
    420-201 – FonctionCl
    Date : 2022-11-03
    Groupe : 2 – lundi & jeudi
    Nom : Piche
    Prénom : Zakari
    DA : 2241035
*/

import java.util.Arrays;

public class FuncBody {
    private String nmFct;
    private LocVar[] params;

    public LocVar[] getParams() {
        return params;
    }

    public String getNmFct() {
        return nmFct;
    }

    public FuncBody(String name, LocVar[] arg) {
        this.nmFct = name;
        this.params = arg;
    }

    public void fctInfo() {
        System.out.printf("CALL: %s, ", nmFct);
        for (int i = 0; i < params.length; i++) {
            System.out.printf("PMS[%d]: %s" + (((params.length - 1) - i == 0) ? "" : ", "), i, params[i].getName());
        }
        System.out.println();
    }
}
