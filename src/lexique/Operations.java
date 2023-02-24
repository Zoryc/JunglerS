/*
    420-201 – Operations
    Date : 2022-11-07
    Groupe : 2 – lundi & jeudi
    Nom : Piche
    Prénom : Zakari
    DA : 2241035
*/
package lexique;

public class Operations {
    public static boolean isOperation(String nm) {
        boolean res = false;
        for (int i = 0; i < nm.length(); i++) {
            if (nm.charAt(i) == '+' || nm.charAt(i) == '-' || nm.charAt(i) == '*' || nm.charAt(i) == '/')
                res = true;
        }
        return res;

    }

    public static boolean isNumber(char c) {
        boolean res = true;

        try {
            Integer.parseInt(String.valueOf(c));
        } catch (Exception e) {
            res = false;
        }

        return res;
    }
}
