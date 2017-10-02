package com.company.Classes;

import java.util.HashMap;

public class Analysis_of_formulas {

    private HashMap<String, Double> variables = new HashMap<>();//Для хранения переменных вводимых с клавиатуры


    //Getter and Setter
    public Double getVariables(String variableName) {
        if(!variables.containsKey(variableName))
        {
            System.out.println("Error: Try get unexists variable '"+variableName+"'");
            return 0.0;
        }
        return variables.get(variableName);
    }

    public void setVariables(String variableName, Double variableValue) {
        this.variables.put(variableName, variableValue);
    }

    public Result Parse(String str)
    {

        return null;//Убрать потом это отседова
    }
}
