package com.company.Classes;

import com.sun.org.apache.regexp.internal.RE;
import com.sun.org.apache.xpath.internal.SourceTree;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    //Functions
    public double Parse(String str) throws Exception {

        str = Del_Space(str);

        System.out.println(str);//ВЫводим безпробельную формулу

        Result result = PlusMinus(str);
        if(!result.remainder.isEmpty())
        {
            System.out.println("Error: can't full parse");
            System.err.println("remainder: " + result.remainder);
        }
        return result.accumulator;
    }


    private Result PlusMinus(String str) throws Exception {

        Result result = Number(str);

        double accumulator;
        char sign;
        String str_without_sign;

        while(!result.remainder.isEmpty() && (result.remainder.charAt(0) == '+' || result.remainder.charAt(0) == '-'))
        {
            sign = result.remainder.charAt(0); //Получаем знак
            str_without_sign = result.remainder.substring(1); // Строка без знака

            accumulator = result.accumulator;

            result = Number(str_without_sign);

            if(sign == '+')
                accumulator += result.accumulator;
            else if(sign == '-')
                accumulator -= result.accumulator;

            result.accumulator = accumulator;

        }
        return new Result(result.accumulator,result.remainder);


    }
    private Result Number(String str) throws Exception {

        boolean negative = false;
        double numberPart;
        String remainderPart;

        Pattern pat = Pattern.compile("[0-9]+\\.?[0-9]*");
        Matcher mat;

        //Если число отрицательно
        if(str.charAt(0) == '-')
        {
            negative = true;
            str = str.substring(1);
        }
        mat = pat.matcher(str);

        //Ищем подстроку, ктр является числом
        if(mat.find())
        {
            numberPart = Double.parseDouble(mat.group());
            remainderPart = str.substring(mat.end());
        }
        else
            throw new Exception("can't get valid number in '" + str + "'");



        if(negative)
            numberPart = -numberPart;


        return new Result(numberPart, remainderPart);
    }


    private String Del_Space(String str)
    {
        //Убираем пробелы
        Pattern pat = Pattern.compile("\\s");
        String mass_str[] = pat.split(str);
        String new_str = "";

        //Слепляем массив строк, в одну строку
        for (String s: mass_str)
            new_str += s;


        return new_str;
    }
}














