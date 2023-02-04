package Projets.yaboi_compiler.Lexique;

public class GlobalTypes {
    public enum gType {
        unknow,
        text,
        number,
        bool,
        instance // instance gonna be later...
    };

    public static gType getGlobalVar(String var) {
        gType gt = gType.number;

        // number first, boolean afterward and lastly... text
        try {
            Integer.parseInt(var);
        } catch (NumberFormatException e) {
            gt = gType.text;
        }

        if (var.equals("true") || var.equals("false")) {
            gt = gType.bool;
        }

        return gt;
    }

}
