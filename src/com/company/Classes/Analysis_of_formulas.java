package com.company.Classes;

import com.sun.org.apache.regexp.internal.RE;
import com.sun.org.apache.xpath.internal.SourceTree;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Analysis_of_formulas {


    //Functions
    public double Parse(String str) throws Exception {

        str = Del_Space(str);//Убираем пробелы

        str = Scientific_record_number(str);//Прячем научную запись числа в переменную

        System.out.println(str);//ВЫводим безпробельную формулу

        input_variable(str); //Анализ не введеных переменных

        Result result = PlusMinus(str);
        if(!result.remainder.isEmpty())
        {
            System.out.println("Error: can't full parse");
            System.err.println("remainder: " + result.remainder);
        }
        return result.accumulator;
    }


    private Result PlusMinus(String str) throws Exception {

        char ch = 0;
        if(str.charAt(0) == '+' || str.charAt(0) == '-')
        {
            ch = str.charAt(0);
            str = str.substring(1);
        }

        Result result = MulDiv(str);

        double accumulator;

        if(ch == '-')
        {
            accumulator = -result.accumulator;
        }
        else
            accumulator = result.accumulator;

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
        if(!str.isEmpty() && str.charAt(0) == '(' && str.charAt(1)!=')')
        {
            Result result = PlusMinus(str.substring(1));

            if(!result.remainder.isEmpty() && result.remainder.charAt(0) == ')')
                result.remainder = result.remainder.substring(1);

            else throw new Exception("The brackets are not closed");

            return result;
        }else
            if(!str.isEmpty() && str.charAt(0) == '(' && str.charAt(1)==')') throw new Exception("In parentheses is empty");

        return FunctionVariable(str);
    }

    //Возвращаем индексы запятых, ктр находятся в скобочках
    private String collect_commas(String str)
    {
        String count_comma = "";
        int count_bracket = 0;

        for(int i = 0, n = str.length(); i < n; i++)
        {
            if(str.charAt(i) == '(')
                count_bracket++;

            if(str.charAt(i) == ')')
                count_bracket--;

            if(str.charAt(i) == ',')
                count_comma = count_comma + Integer.toString(i) + " ";

            if(count_bracket == 0)
            {
                if(!count_comma.equals(""))
                    count_comma = count_comma + Integer.toString(i) + " ";
                break;
            }
        }
        return count_comma;
    }

    private Result FunctionVariable(String str) throws Exception {

        Pattern pat = Pattern.compile("[A-Z,a-z]+[A-Z,a-z0-9]*");
        Matcher mat = pat.matcher(str);

        if(mat.find() && mat.start() == 0) // Ищем функцию или переменную
        {
            Result result = new Result();

            if(!str.substring(mat.end()).isEmpty() && str.charAt(mat.end())== '('){ //Нашли функцию

                String s = collect_commas(str.substring(mat.end()));
                ArrayList<Double> arrayList = new ArrayList();

                if(s.equals(""))//Если один аргумент функции
                {
                    result = Brackets(str.substring(mat.end()));
                    arrayList.add(result.accumulator);
                    result.accumulator = Function(mat.group(),arrayList);
                }
                else//Несколько аргументов
                {
                    Pattern pat1 = Pattern.compile("[0-9]+");
                    Matcher mat1 = pat1.matcher(s);

                    int start_interval = 0, end_interval = 0;

                    while (mat1.find())
                    {
                        end_interval = Integer.parseInt(mat1.group());
                        arrayList.add(Brackets(str.substring(mat.end() + start_interval + 1, mat.end() + end_interval)).accumulator);
                        start_interval = end_interval;
                    }
                    result.remainder = str.substring(mat.end() + end_interval+1);
                    result.accumulator = Function(mat.group(),arrayList);
                }

            }
            else //Нашли переменную
            {
                result.accumulator = Storage.getVariables(mat.group());
                result.remainder = str.substring(mat.end());
            }
            return new Result(result.accumulator, result.remainder);
        }

        return Number(str);
    }


    private double Function(String name_function, ArrayList arrayList) throws Exception {

        name_function = name_function.toLowerCase();//Переводим название функции в нижний регистр

        double result_math_function = 0;

        switch (name_function)
        {
            case "sin":
                result_math_function = Math.sin(Math.toRadians( (double)arrayList.get(0) ));
                break;
            case "cos":
                result_math_function = Math.cos(Math.toRadians((double)arrayList.get(0)));
                break;
            case "pow":
                result_math_function = Math.pow((double)arrayList.get(0),(double)arrayList.get(1));
                break;
            case "log":
                result_math_function = Math.log((double)arrayList.get(0));
                break;
            case "abs":
                result_math_function = Math.abs((double)arrayList.get(0));
                break;
            case "mod":
                result_math_function = (double)arrayList.get(0) % (double)arrayList.get(1);
                break;
            case "sqrt":
                result_math_function = Math.sqrt((double)arrayList.get(0));
                break;
            case "ceil":
                result_math_function = Math.ceil((double)arrayList.get(0));
                break;
            case "floor":
                result_math_function = Math.floor((double)arrayList.get(0));
                break;
                default:{
                    String s = Storage.getFunctions(name_function);

                    if(s != null)
                    {
                        Pattern pat1, pat2;
                        Matcher mat1, mat2;

                        String new_str = s;

                        //Удаляем функции
                        pat1 = Pattern.compile("[A-Za-z]+[A-Za-z0-9]*\\(");
                        mat1 = pat1.matcher(new_str);

                        new_str = mat1.replaceAll("");

                        //Ищем в строке без функций названия переменных
                        pat2 = Pattern.compile("[A-Za-z]+[A-Za-z0-9]*");//Шаблон переменной
                        mat2 = pat2.matcher(new_str);

                        ArrayList<String> mass_str = new ArrayList();

                        for(int i = 0; mat2.find(); i++)
                        {
                            if(mass_str.contains(mat2.group()))
                            {
                                s = s.replaceFirst(mat2.group(), String.valueOf(arrayList.get(mass_str.indexOf(mat2.group()))));
                                continue;
                            }
                            mass_str.add(mat2.group());

                            s = s.replaceFirst(mat2.group(), String.valueOf(arrayList.get(i)));
                        }
                        result_math_function = PlusMinus(s).accumulator;
                    }
                }
        }
        return result_math_function;
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
        if(mat.find()&& mat.start() == 0)
        {
            numberPart = Double.parseDouble(mat.group());
            remainderPart = str.substring(mat.end());
        }
        else
        {
            Result res = PlusMinus(str);
            numberPart = res.accumulator;
            remainderPart = res.remainder;
            //throw new Exception("can't get valid number in '" + str + "'");
        }


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

    private void input_variable(String str)
    {
        Pattern pat1, pat2;
        Matcher mat1, mat2;

        String new_str = str;

        //Удаляем функции
        pat1 = Pattern.compile("[A-Za-z]+[A-Za-z0-9]*\\(");
        mat1 = pat1.matcher(new_str);

        new_str = mat1.replaceAll("");
        //System.out.println(new_str);

        //Ищем в строке без функций названия переменных
        pat2 = Pattern.compile("[A-Za-z]+[A-Za-z0-9]*");//Шаблон переменной
        mat2 = pat2.matcher(new_str);

        Scanner sc = new Scanner(System.in);

        while(mat2.find())
        {
            String s = mat2.group();
            if(Storage.getVariables(mat2.group()) == Double.MIN_VALUE)
            {
                System.out.println("Input variable " + mat2.group()+": ");
                Storage.setVariables(mat2.group(),Double.parseDouble(sc.next()));
            }
        }
    }

    private String Scientific_record_number(String str) //Парсим научную запись числа
    {
        Pattern pat = Pattern.compile("[0-9]+\\.?[0-9]*[Ee]+?[\\+\\-]?[0-9]+");
        Matcher mat = pat.matcher(str);

        while(mat.find())
        {
            double temp_double = Double.parseDouble(mat.group());

            //Генерируем название переменной ктр будет хранить научную запись числа
            String dict = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
            int str_length = 4;

            Random rnd = new Random();
            String name_var = "";

            for(int i = 0; i < str_length; i++)
            {
                int index = rnd.nextInt(dict.length());
                name_var += dict.charAt(index);
            }
            //Заносим в hashmap переменную и значение числа
            Storage.setVariables(name_var, temp_double);
            str = mat.replaceFirst(name_var);
            mat = pat.matcher(str);
        }
        return str;
    }
}














