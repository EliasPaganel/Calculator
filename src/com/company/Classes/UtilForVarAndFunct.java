package com.company.Classes;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilForVarAndFunct {

    private static Map<String, Double> variables = new HashMap<>();//Для хранения переменных вводимых с клавиатуры

    private static Map<String, String> functions = new HashMap<>(); //Для хранения функция вводимых с клавиатуры

    //Getter and Setter
    public static Double getVariables(String variableName) {
        if(!variables.containsKey(variableName)) {
            return Double.MIN_VALUE;
        }
        return variables.get(variableName);
    }

    public static String getFunctions(String functionName) {
        if(!functions.containsKey(functionName)) {
            return null;
        }
        return functions.get(functionName);
    }

    public static void setVariables(String variableName, Double variableValue) {
        variables.put(variableName, variableValue);
    }

    public static void setFunctions(String functionName, String functionBody) {
        functions.put(functionName, functionBody);
    }

    //Functions
    public static String initVarFunc(String inputString) {
        Scanner sc = new Scanner(System.in);
        while (Pattern.matches("var.+", inputString) || Pattern.matches("func.+", inputString)) {

            if(Pattern.matches("var.+", inputString)) {
                //Парсим имя переменной
                Pattern pat = Pattern.compile("var|=.+|\\s");
                Matcher mat = pat.matcher(inputString);
                String varName = mat.replaceAll("");
                //Парсим значение переменной
                Pattern pat2 = Pattern.compile(".+=|\\s");
                Matcher mat2 = pat2.matcher(inputString);
                String varValue = mat2.replaceAll("");
                if(!varName.equals("") && !varValue.equals(""))
                    UtilForVarAndFunct.setVariables(varName, Double.parseDouble(varValue));
            }
            else if(Pattern.matches("func.+", inputString)) {
                //Парсим имя функции
                Pattern pat = Pattern.compile("func|=.+|\\s");
                Matcher mat = pat.matcher(inputString);
                String funcName = mat.replaceAll("");
                //Парсим тело функции
                Pattern pat2 = Pattern.compile(".+=|\\s");
                Matcher mat2 = pat2.matcher(inputString);
                String funcBody = mat2.replaceAll("");
                if(!funcName.equals("") && !funcBody.equals(""))
                    UtilForVarAndFunct.setFunctions(funcName, funcBody);
            }
            inputString =  sc.nextLine();
        }
        return inputString;
    }
}
