/*
    420-201 – LocVar
    Date : 2022-11-09
    Groupe : 2 – lundi & jeudi
    Nom : Piche
    Prénom : Zakari
    DA : 2241035
*/
package structures;

import lexique.GlobalTypes;

public class LocVar {
    private static final GlobalTypes.gType TYPE_DEFAULT = GlobalTypes.gType.unknow;
    private static final String NAME_DEFAULT = "null";
    private static final String VALUE_DEFAULT = "null";
    private GlobalTypes.gType type;
    private String name;
    private Object value;

    public GlobalTypes.gType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public LocVar() {
        this(TYPE_DEFAULT, NAME_DEFAULT, VALUE_DEFAULT);
    }

    public LocVar(GlobalTypes.gType type, String name, Object val) {
        this.type = type;
        this.name = name.trim();
        this.value = val.toString().trim();
    }
    public void showVars() {
        System.out.printf("LOC: %s, NAME: %s, VAL: %s\n", type.toString(), name, value.toString());
    }
}
