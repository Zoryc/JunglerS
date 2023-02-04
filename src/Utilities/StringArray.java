/*
    420-201 – StringArray
    Date : 2022-11-14
    Groupe : 2 – lundi & jeudi
    Nom : Piche
    Prénom : Zakari
    DA : 2241035
*/
package Projets.yaboi_compiler.Utilities;

import java.util.Arrays;

public class StringArray {
    public static void makeNullToString(String[] tb) {
        for (int i = 0; i < tb.length; i++) {
            if (tb[i] == null)
                tb[i] = "";
        }
    }
    public static String[] createStringArray() {
        return new String[0];
    }

    public static String[] resizeStringArray(String[] arr, int nbRe) {
        String[] res = Arrays.copyOf(arr, nbRe);

        for (int i = 0; i < res.length; i++) {
            if (arr.length <= i)
                res[i] = "";
        }
        return res;
    }

    public static boolean searchStringArray(String[] tbS, String str) {
        for (int i = 0; i < tbS.length; i++) {
            if (Utilities.isExactString(tbS[i], str))
                return true;
        }
        return false;
    }

    public static String[] addElement(String[] tbs, String elem) {
        String[] tb = resizeStringArray(tbs, tbs.length + 1);

        tb[tbs.length] = elem;

        return tb;
    }
}
