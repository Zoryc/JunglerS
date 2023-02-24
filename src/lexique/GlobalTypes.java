package lexique;

public class GlobalTypes {
    public enum gType {
        unknow,
        text,
        number,
        bool,
        table,
        closure,
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

        if (!var.isEmpty() && var.charAt(0) == '{' && var.charAt(var.length() - 1) == '}')
            gt = gType.table;

        // closure test
        char current;
        int test = 0;
        for (int i = 0; i < var.length(); i++) {
            current = var.charAt(i);
            if (current == '(' || current == ')')
                test++;
        }
        if (test == 2)
            gt = gType.closure;

        return gt;
    }

}
