/*
    420-201 – StringArray
    Date : 2022-11-14
    Groupe : 2 – lundi & jeudi
    Nom : Piche
    Prénom : Zakari
    DA : 2241035
*/
package tools;

import java.util.Arrays;

public class StringArray {
    private String[] arr;

    public String[] getArr() {
        return arr;
    }

    public StringArray(String[] ars) {
        String[] newA = new String[ars.length];

        for (int i = 0; i < ars.length; i++) {
            newA[i] = ars[i];
        }

        arr = newA;
    }

    public StringArray() {
        this(new String[0]);
    }

    public int size() {
        return arr.length;
    }

    private static String[] resizeStringArray(String[] arr, int nbRe) {
        String[] res = Arrays.copyOf(arr, nbRe);

        for (int i = 0; i < res.length; i++) {
            if (arr.length <= i)
                res[i] = "";
        }
        return res;
    }

    public static boolean searchStringArray(String[] tbS, String str) {
        for (int i = 0; i < tbS.length; i++) {
            if (Multi.isExactString(tbS[i], str))
                return true;
        }
        return false;
    }

    public String getElement(int nm) {
        return arr[nm];
    }

    public void addElement(String elem) {
        String[] tb = resizeStringArray(arr, arr.length + 1);

        tb[arr.length] = elem;

        arr = tb;
    }

    public String newValue(String elem, int nm) {
        String old = arr[nm];

        arr[nm] = elem;

        return old;
    }

    public void appendElement(String elem, int nm) {
        arr[nm] += elem;
    }
}
