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

        Result result = MulDiv(str);
        double accumulator = result.accumulator;

        while(!result.remainder.isEmpty() && (result.remainder.charAt(0) == '+' || result.remainder.charAt(0) == '-'))
        {
            char sign = result.remainder.charAt(0); //Получаем знак
            String str_without_sign = result.remainder.substring(1); // Строка без знака

            result = MulDiv(str_without_sign);

            if(sign == '+')
                accumulator += result.accumulator;
            else if(sign == '-')
                accumulator -= result.accumulator;

        }
        return new Result(accumulator,result.remainder);
    }


    private Result MulDiv(String str) throws Exception {

        Result result = Brackets(str);
        double accumulator = result.accumulator;

        while(!result.remainder.isEmpty() && (result.remainder.charAt(0) == '*' || result.remainder.charAt(0) == '/'))
        {
            char sign = result.remainder.charAt(0);//Получаем знак
            String str_without_sign = result.remainder.substring(1);

            result = Brackets(str_without_sign);

            if(sign == '*')
                accumulator *= result.accumulator;
            else if(sign == '/')
                accumulator /= result.accumulator;

        }

        return new Result(accumulator, result.remainder);
    }


    private Result Brackets(String str) throws Exception
    {
        if(!str.isEmpty() && str.charAt(0) == '(')
        {
            Result result = PlusMinus(str.substring(1));

            if(!result.remainder.isEmpty() && result.remainder.charAt(0) == ')')
                result.remainder = result.remainder.substring(1);

            else throw new Exception("The brackets are not closed");

            return result;
        }

        return FunctionVariable(str);
    }

    private Result FunctionVariable(String str) throws Exception {
        return Number(str);
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
        else if(str.charAt(0) == '+')
            str = str.substring(1);


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

        return Brackets_problem(new_str);
    }

    private String Brackets_problem(String str)
    {
        String str_new = str;

        if(!str.isEmpty() && str.charAt(0) == '+' && str.charAt(1) == '(') str_new = str.substring(1);
        if(!str.isEmpty() && str.charAt(0) == '-' && str.charAt(1) == '(')
            str_new = "-1*" + str.substring(1);

        return str_new;

    }
}














