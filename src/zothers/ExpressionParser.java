package zothers;/*
    420-201 – Parser
    Date : 2022-11-03
    Groupe : 2 – lundi & jeudi
    Nom : Piche
    Prénom : Zakari
    DA : 2241035
*/

import lexique.Declaration;
import lexique.GlobalTypes;
import lexique.Operations;
import structures.Closure;
import structures.FuncBody;
import structures.LocVar;
import separateloader.Environnements;
import structures.ReturnVal;
import tools.StringArray;
import tools.Multi;

import java.lang.reflect.Parameter;

public class ExpressionParser {
    private String errorMessage;
    private StringArray codeLines;
    private ScriptStack stackCl;
    private Environnements envr;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setCodeLines(StringArray codeLines) {
        this.codeLines = codeLines;
    }

    public ScriptStack getStackCl() {
        return stackCl;
    }

    public ExpressionParser(int[] opts, Environnements evr) {
        envr = evr;
        errorMessage = new String();
        stackCl = new ScriptStack();
        this.codeLines = new StringArray();
    }

    public void postProcessing(String sc) {
        convertLines(sc);
        System.out.print("[POSTPROCESSING] ");
        Multi.afficherTableau(codeLines.getArr());
    }

    public boolean parseScript() {
        boolean res = parseWords();
        return res;
    }

    private LocVar searchVarStack(String name) { // TEMPORARY
        return Optimiser.searchVars(stackCl.getRawStack(), name, stackCl.getRawStack().length - 1);
    }

    private Closure searchClosureStack(String name) { // TEMPORARY
        return Optimiser.searchClosure(stackCl.getRawStack(), name, stackCl.getRawStack().length - 1);
    }

    private boolean parseWords() {
        String word;
        StringArray params;
        boolean inParameters;
        boolean inString;
        int nbParams;

        for (int i = 0; i < codeLines.size() && errorMessage.isEmpty(); i++) {
            params = new StringArray();
            word = new String();
            inParameters = false;
            inString = false;
            nbParams = 0;

            for (int j = 0; j < codeLines.getElement(i).length(); j++) {
                if (codeLines.getElement(i).charAt(j) != '(' && codeLines.getElement(i).charAt(j) != ' ') {
                    word += codeLines.getElement(i).charAt(j);
                } else {
                    break;
                }
            }

            word = word.trim();

            LocVar varRes = searchVarStack(word); // VERY TEMPORARY THING!!!!!!
            Closure closRes = searchClosureStack(word);
            String varLex = Declaration.getDeclType(word);

            if (envr.isPresent(word) || closRes != null) {
                for (int j = word.length(); j < codeLines.getElement(i).length(); j++) {

                    if (!inParameters && codeLines.getElement(i).charAt(j) == '(') {
                        inParameters = true;
                        continue;
                    } else if (inParameters && codeLines.getElement(i).charAt(j) == ')' && !inString) {
                        inParameters = false;
                    } else if (inParameters && codeLines.getElement(i).charAt(j) == '.' && !inString) {
                        inString = true;
                    } else if (inParameters && codeLines.getElement(i).charAt(j) == '.' && inString) {
                        inString = false;
                        params.newValue(params.getElement(nbParams).trim(), nbParams);
                    }

                    if (inParameters && codeLines.getElement(i).charAt(j) == ',' && !inString) {
                        nbParams++;
                        params.addElement(new String());
                    } else if (inParameters) {
                        if (params.size() == 0)
                            params.addElement(new String());

                        params.appendElement(String.valueOf(codeLines.getElement(i).charAt(j)), nbParams);
                    }
                }

                LocVar[] paramsTmp = new LocVar[params.size()]; // VERY TEMPORARY
                for (int j = 0; j < params.size(); j++) {
                    paramsTmp[j] = new LocVar(GlobalTypes.gType.unknow, params.getElement(j), "null");
                }

                stackCl.addElement(new FuncBody(word, paramsTmp));
            } else if (varLex == "declaration" || varRes != null) {
                boolean scannedName = false;
                boolean hasVar = false;
                char cC;

                String name = "";
                String value = "";

                if (varRes == null) { // check if it not existing before creating definition
                    // get def name and value...
                    for (int j = word.length(); j < codeLines.getElement(i).length(); j++) {
                        cC = codeLines.getElement(i).charAt(j);

                        if (!scannedName && cC != ' ')
                            name += cC;
                        else if (!scannedName && cC == ' ' && !name.isEmpty())
                            scannedName = true;
                        else if (scannedName && !hasVar && cC == '=')
                            hasVar = true;
                        else if (scannedName && hasVar) {
                            value += cC;
                        }
                    }

                    value = value.trim();

                    // check if it alr existing...
                    if (searchVarStack(name) == null && !Declaration.isDeclNil(name)) // check if it already defined and not put in the stack (skip them)
                        stackCl.addElement(new LocVar(GlobalTypes.getGlobalVar(value), name, value.isEmpty() ? "null" : value));
                    else {
                        errorMessage = "variable redifinition for: \"" + name + "\" at line " + (i + 1);
                        return true;
                    }

                } else {
                    // reassign existing definition(s)
                    for (int j = 0; j < codeLines.getElement(i).length(); j++) {
                        cC = codeLines.getElement(i).charAt(j);

                        if (!scannedName && cC != ' ')
                            name += cC;
                        else if (!scannedName && cC == ' ' && !name.isEmpty())
                            scannedName = true;
                        else if (scannedName && !hasVar && cC == '=')
                            hasVar = true;
                        else if (scannedName && hasVar) {
                            value += cC;
                        }

                        if (cC == ')' && !scannedName) {
                            errorMessage = "attempting to call a variable at line " + (i + 1);
                            return true;
                        }
                    }

                    if (!Declaration.isDeclNil(name))
                        stackCl.addElement(new LocVar(varRes.getType(), name, value));
                }
            } else if (varLex == "block") {
                String nameC = new String();
                boolean scannedName = false;
                String blocks[];
                int numBl = 0;
                int sizeBlck = 0;
                int lastPos = i;

                params.addElement(new String());

                for (int j = word.length(); j < codeLines.getElement(i).length(); j++) {
                    if (codeLines.getElement(i).charAt(j) != '(' && !scannedName) {
                        nameC += codeLines.getElement(i).charAt(j);
                    } else if (codeLines.getElement(i).charAt(j) == '(' && !scannedName) {
                        nameC = nameC.trim();
                        scannedName = true;
                        inParameters = true;
                    } else if (inParameters && codeLines.getElement(i).charAt(j) == ')') {
                        inParameters = false;
                    } else if (inParameters && codeLines.getElement(i).charAt(j) != ',') {
                        params.appendElement(String.valueOf(codeLines.getElement(i).charAt(j)), nbParams);
                    } else if (inParameters && codeLines.getElement(i).charAt(j) == ',') {
                        params.addElement(new String());
                        nbParams++;
                    }
                }

                if (searchClosureStack(nameC) != null || searchVarStack(nameC) != null) {
                    errorMessage = "closure redifinition for: \"" + nameC + "\" at line " + (i + 1);
                    return true;
                }


                // Closure class will do the most of the jobs here :D
                for (int j = i + 1; j < codeLines.size(); j++) {
                    if (codeLines.getElement(j).equals("fend")) {
                        break;
                    }
                    sizeBlck++;
                }

                blocks = new String[sizeBlck];

                for (int j = i + 1; j < codeLines.size() && !codeLines.getElement(j).equals("fend"); j++) {
                    blocks[numBl++] = codeLines.getElement(j);
                }

                i += sizeBlck + 1; // skip the block to not place it in the stack + the fend skip

                stackCl.addElement(new Closure(blocks, nameC, params.getArr(), lastPos));
            } else if (word.equals("return")) {
                String ret = new String();
                for (int j = word.length(); j < codeLines.getElement(i).length(); j++) {
                    ret += codeLines.getElement(i).charAt(j);
                }
                ret = ret.trim();
                stackCl.addElement(new ReturnVal(new LocVar(GlobalTypes.getGlobalVar(ret), "", ret)));
            } else {
                errorMessage = (!word.isEmpty() ? word + " is unknow symbol" : "???");
                return true;
            }
        }

        return false;
    }

    public int parseOperation(String op) { // already verified that it an operation
        int res = 0;
        int currNum = 0;
        char opHold = 0;


        for (int i = 0; i < op.length(); i++) {
            char curr = op.charAt(i);

            if (Operations.isNumber(curr) && opHold == 0) {
                currNum = Integer.parseInt(String.valueOf(curr));
            } else if (currNum != 0 && opHold == 0) {
                switch (curr) {
                    case '+':
                    case '-':
                    case '*':
                    case '/':
                        opHold = curr;
                }
            }

            if ((res != 0 || currNum != 0) && opHold != 0 && Operations.isNumber(curr)) {
                int numf = Integer.parseInt(String.valueOf(curr));

                switch (opHold) {
                    case '+':
                        res += (res == 0) ? currNum + numf : numf;
                        break;
                    case '-':
                        res += (res == 0) ? currNum - numf : -numf;
                        break;
                    case '*':
                        if (res == 0) {
                            res = currNum * numf;
                        }
                        res *= numf;
                        break;
                    case '/':
                        if (res == 0) {
                            res = currNum / numf;
                        }
                        res /= numf;
                        break;
                }

                opHold = 0;
            }

        }

        return res;
    }
    
    public String[] parseArray(String arr) {
        StringArray pArr = new StringArray();
        boolean isEnd = false;

        for (int i = 1, e = 0; i < arr.length() && !isEnd; i++) { // e used for new parsed array and start at 1 for skipping { first
            if (arr.charAt(i) == '}')
                isEnd = true;
            else if (arr.charAt(i) != ',') {
                if (e == pArr.size() || e == 0)
                    pArr.addElement("");
                pArr.appendElement(String.valueOf(arr.charAt(i)), e);
            } else if (arr.charAt(i) == ',')
                e++;
        }

        return pArr.getArr();
    }

    private void convertLines(String scrpt) {
        String declEnd = new String();
        char cuC;

        for (int i = 0; i < scrpt.length(); i++) {
            cuC = scrpt.charAt(i);

            if (!Multi.isExactChar(cuC, ';')) {
                declEnd += cuC;
            }

            if (!declEnd.isEmpty() && cuC == ';') {
                codeLines.addElement(declEnd.trim());
                System.out.println("[CONVERTLINES] Added \"" + declEnd.trim() + "\"");
                declEnd = "";
            }
        }

        if (codeLines.size() == 0) {
            errorMessage = "Syntax Error";
        }
    }

    public void showDebugResults() {
        boolean resFail = !getErrorMessage().isEmpty();

        System.out.println("[PARSER] STATUS " + ((resFail) ? "FAILED" : "OK"));

        if (!resFail) {
            System.out.println("[PARSER] Stack DEBUG");
            stackCl.showDebug();
            System.out.println("[PARSER] Stack DEBUG End");
        }
    }
}
