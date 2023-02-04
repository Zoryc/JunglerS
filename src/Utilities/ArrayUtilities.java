/*
    420-201 – ArrayUtilities
    Date : 2022-11-11
    Groupe : 2 – lundi & jeudi
    Nom : Piche
    Prénom : Zakari
    DA : 2241035
*/
package Projets.yaboi_compiler.Utilities;

import java.util.Arrays;

public class ArrayUtilities {
    public static Object[] createArray(int nbElem) {
        Object[] res = new Object[nbElem];

        for (int i = 0; i < res.length; i++) {
            res[i] = null;
        }

        return res;
    }

    public static Object[] resizeArray(Object[] arr, int nbRe) {
        Object[] res = Arrays.copyOf(arr, nbRe);

        for (int i = 0; i < res.length; i++) {
            if (arr.length <= i)
                res[i] = null;
        }
        return res;
    }
}
