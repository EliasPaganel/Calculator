package com.company.Classes;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Storage {

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
    public static String varFunc(String s) {
        Scanner sc = new Scanner(System.in);
        while (Pattern.matches("var.+", s) || Pattern.matches("func.+",s)) {

            if(Pattern.matches("var.+", s)) {
                //Парсим имя переменной
                Pattern pat = Pattern.compile("var|=.+|\\s");
                Matcher mat = pat.matcher(s);
                String varName = mat.replaceAll("");
                //Парсим значение переменной
                Pattern pat2 = Pattern.compile(".+=|\\s");
                Matcher mat2 = pat2.matcher(s);
                String varValue = mat2.replaceAll("");
                if(!varName.equals("") && !varValue.equals(""))
                    Storage.setVariables(varName, Double.parseDouble(varValue));
            }
            else if(Pattern.matches("func.+", s)) {
                //Парсим имя функции
                Pattern pat = Pattern.compile("func|=.+|\\s");
                Matcher mat = pat.matcher(s);
                String funcName = mat.replaceAll("");
                //Парсим тело функции
                Pattern pat2 = Pattern.compile(".+=|\\s");
                Matcher mat2 = pat2.matcher(s);
                String funcBody = mat2.replaceAll("");
                if(!funcName.equals("") && !funcBody.equals(""))
                    Storage.setFunctions(funcName, funcBody);
            }
            s =  sc.nextLine();
        }
        return s;
    }
}
