package Projets.yaboi_compiler;
/*
    420-201 – Parser
    Date : 2022-11-03
    Groupe : 2 – lundi & jeudi
    Nom : Piche
    Prénom : Zakari
    DA : 2241035
*/

import Projets.yaboi_compiler.Lexique.Declaration;
import Projets.yaboi_compiler.Misc.Closure;
import Projets.yaboi_compiler.Misc.FuncBody;
import Projets.yaboi_compiler.Misc.LocVar;
import Projets.yaboi_compiler.SeparateLoader.Environnements;
import Projets.yaboi_compiler.Utilities.StringArray;
import Projets.yaboi_compiler.Utilities.Utilities;

public class Parser {
    private String errorMessage;

    private String[] codeLines;
    private ScriptStack stackCl;
    private Environnements envr;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setCodeLines(String[] codeLines) {
        this.codeLines = codeLines;
    }

    public ScriptStack getStackCl() {
        return stackCl;
    }

    public Parser(int[] opts, Environnements evr) {
        envr = evr;
        errorMessage = new String();
        stackCl = new ScriptStack();
        this.codeLines = StringArray.createStringArray();
    }

    public void postProcessing(String sc) {
        convertLines(sc);
        System.out.print("[POSTPROCESSING] ");
        Utilities.afficherTableau(codeLines);
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
        String params[];
        boolean inParameters;
        boolean inString;
        int nbParams;

        for (int i = 0; i < codeLines.length && errorMessage.isEmpty(); i++) {
            params = StringArray.createStringArray();
            word = new String();
            inParameters = false;
            inString = false;
            nbParams = 0;

            for (int j = 0; j < codeLines[i].length(); j++) {
                if (codeLines[i].charAt(j) != '(' && codeLines[i].charAt(j) != ' ') {
                    word += codeLines[i].charAt(j);
                } else {
                    break;
                }
            }

            word = word.trim();

            LocVar varRes = searchVarStack(word); // VERY TEMPORARY THING!!!!!!
            Closure closRes = searchClosureStack(word);
            String varLex = Declaration.getDeclType(word);

            if (envr.isPresent(word) || closRes != null) {
                for (int j = word.length(); j < codeLines[i].length(); j++) {

                    if (!inParameters && codeLines[i].charAt(j) == '(') {
                        inParameters = true;
                        continue;
                    } else if (inParameters && codeLines[i].charAt(j) == ')' && !inString) {
                        inParameters = false;
                    } else if (inParameters && codeLines[i].charAt(j) == '.' && !inString) {
                        inString = true;
                    } else if (inParameters && codeLines[i].charAt(j) == '.' && inString) {
                        inString = false;
                        params[nbParams] = params[nbParams].trim();
                    }

                    if (inParameters && codeLines[i].charAt(j) == ',' && !inString) {
                        params = StringArray.addElement(params, new String());
                        nbParams++;
                    } else if (inParameters) {
                        if (params.length == 0)
                            params = StringArray.addElement(params, new String());

                        params[nbParams] += codeLines[i].charAt(j);
                    }
                }

                stackCl.addElement(new FuncBody(word, params));
            } else if (varLex == "declaration" || varRes != null) {
                boolean scannedName = false;
                boolean hasVar = false;
                char cC;

                String name = "";
                String value = "";

                if (varRes == null) { // check if it not existing before creating definition
                    // get def name and value...
                    for (int j = word.length(); j < codeLines[i].length(); j++) {
                        cC = codeLines[i].charAt(j);

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

                    // check if it alr existing...
                    if (searchVarStack(name) == null && !Declaration.isDeclNil(name)) // check if it already defined and not put in the stack (skip them)
                        stackCl.addElement(new LocVar(word, name, value));

                } else {
                    // reassign existing definition(s)
                    for (int j = 0; j < codeLines[i].length(); j++) {
                        cC = codeLines[i].charAt(j);

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

                    if (!Declaration.isDeclNil(name))
                        stackCl.addElement(new LocVar(varRes.getType(), name, value));
                }
            } else if (varLex == "block") {
                String nameC = new String(); // GOOD
                boolean scannedName = false;
                String blocks[];
                int numBl = 0;
                int sizeBlck = 0;
                int lastPos = i;

                params = StringArray.addElement(params, new String());

                for (int j = word.length(); j < codeLines[i].length(); j++) {
                    if (codeLines[i].charAt(j) != '(' && !scannedName) {
                        nameC += codeLines[i].charAt(j);
                    } else if (codeLines[i].charAt(j) == '(' && !scannedName) {
                        nameC = nameC.trim();
                        scannedName = true;
                        inParameters = true;
                    } else if (inParameters && codeLines[i].charAt(j) == ')') {
                        inParameters = false;
                    } else if (inParameters && codeLines[i].charAt(j) != ',') {
                        params[nbParams] += codeLines[i].charAt(j);
                    } else if (inParameters && codeLines[i].charAt(j) == ',') {
                        params = StringArray.addElement(params, new String());
                        nbParams++;
                    }
                }

                // Closure class will do the most of the jobs here :D
                for (int j = i + 1; j < codeLines.length; j++) {
                    if (codeLines[j].equals("fend")) {
                        break;
                    }
                    sizeBlck++;
                }

                blocks = new String[sizeBlck];

                for (int j = i + 1; j < codeLines.length; j++) {
                    if (codeLines[j].equals("fend")) {
                        break;
                    }
                    blocks[numBl++] = codeLines[j];
                }

                i += sizeBlck + 1; // skip the block to not place it in the stack + the fend

                stackCl.addElement(new Closure(blocks, nameC, params, lastPos));
            } else {
                errorMessage = (!word.isEmpty() ? word + " is unknow symbol" : "???");
                return true;
            }
        }

        return false;
    }

    private void convertLines(String scrpt) {
        String declEnd = new String();
        char cuC;

        for (int i = 0; i < scrpt.length(); i++) {
            cuC = scrpt.charAt(i);

            if (!Utilities.isExactChar(cuC, ';')) {
                declEnd += cuC;
            }

            if (!declEnd.isEmpty() && cuC == ';') {
                codeLines = StringArray.addElement(codeLines, declEnd.trim());
                System.out.println("[CONVERTLINES] Added \"" + declEnd.trim() + "\"");
                declEnd = "";
            }
        }

        if (codeLines.length == 0) {
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
