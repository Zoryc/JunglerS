/*
    420-201 – Lexique
    Date : 2022-11-07
    Groupe : 2 – lundi & jeudi
    Nom : Piche
    Prénom : Zakari
    DA : 2241035
*/
package Projets.yaboi_compiler.Lexique;

import Projets.yaboi_compiler.Utilities.StringArray;

public class Declaration {
    static String[] dictFcl = {
    };

    static String dictAttr[] = {
            "gbl"
    };

    static String dictSign[] = {
            "fonction"
    };

    public static String getDeclType(String part) {
        String fctType = new String();

        if (StringArray.searchStringArray(dictFcl, part))
            fctType = "fonction";
        else if (StringArray.searchStringArray(dictAttr, part))
            fctType = "declaration";
        else if (StringArray.searchStringArray(dictSign, part))
            fctType = "block";

        return fctType;
    }

    public static boolean isDeclNil(String name) {
        return getDeclType(name) == new String();
    }

}
