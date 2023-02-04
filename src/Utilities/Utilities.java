/*
    420-201 – Utilities
    Date : 2022-11-07
    Groupe : 2 – lundi & jeudi
    Nom : Piche
    Prénom : Zakari
    DA : 2241035
*/
package Projets.yaboi_compiler.Utilities;

public class Utilities {

    public static boolean isExactChar(char c1, char c2) {
        return c1 == c2;
    }

    public static boolean isExactString(String str1, String str2) {
        return str1.equals(str2);
    }

    public static void afficherTableau(String tb[]) {
        System.out.printf("Array length: %d\n", tb.length);
        System.out.print("[");
        for (int i = 0; i < tb.length; i++) {
            System.out.print((i == 0 ? "" : ", ") + tb[i]);
        }
        System.out.println("]");
    }

    public static String reverseString(String st) {
        String res = new String();
        for (int i = 0; i < st.length(); i++) {
            res += st.charAt(st.length() - i - 1);
        }
        return res;
    }

}
