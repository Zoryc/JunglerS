/*
    420-201 – LocVar
    Date : 2022-11-09
    Groupe : 2 – lundi & jeudi
    Nom : Piche
    Prénom : Zakari
    DA : 2241035
*/
package Projets.yaboi_compiler.Misc;

public class LocVar {
    private static final String TYPE_DEFAULT = "null";
    private static final String NAME_DEFAULT = "null";
    private static final String VALUE_DEFAULT = "null";
    private String type;
    private String name;
    private Object value;

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public LocVar() {
        this(TYPE_DEFAULT, NAME_DEFAULT, VALUE_DEFAULT);
    }

    public LocVar(String type, String name, Object val) {
        this.type = type.trim();
        this.name = name.trim();
        this.value = val.toString().trim();
    }
    public void showVars() {
        System.out.printf("VAR TYPE: %s, NAME: %s, VAL: %s\n", type, name, value.toString().trim());
    }
}
