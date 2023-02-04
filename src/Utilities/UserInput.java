/*
    420-201 – UserInput
    Date : 2022-11-17
    Groupe : 2 – lundi & jeudi
    Nom : Piche
    Prénom : Zakari
    DA : 2241035
*/
package Projets.yaboi_compiler.Utilities;

import java.util.Scanner;

public class UserInput {

    public static String getUserText() {
        String responseInput = new String();
        String text = new String();
        Scanner sc = new Scanner(System.in);

        do {
            text += responseInput;

            responseInput = sc.nextLine();
        } while (!responseInput.trim().equalsIgnoreCase("stop"));

        return text;
    }
}
