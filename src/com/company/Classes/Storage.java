package com.company.Classes;

import java.util.HashMap;

public class Storage {

    private static HashMap<String, Double> variables = new HashMap<>();//Для хранения переменных вводимых с клавиатуры

    private static HashMap<String, String> functions = new HashMap<>(); //Для хранения функция вводимых с клавиатуры


    //Getter and Setter
    public static Double getVariables(String variableName) {
        if(!variables.containsKey(variableName))
        {
            return Double.MIN_VALUE;
        }
        return variables.get(variableName);
    }

    public static String getFunctions(String functionName) {
        if(!functions.containsKey(functionName))
        {
            return null;
        }
        return functions.get(functionName);
    }

    public static void setVariables(String variableName, Double variableValue) {
        variables.put(variableName, variableValue);
    }

    public static void setFunctions(String functionName, String function_body) {
        functions.put(functionName, function_body);
    }
}
