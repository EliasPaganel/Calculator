package com.company.Classes;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnalysisOfTheFormula {

    //Functions
    public double calculate(String str) throws Exception {

        str = delSpace(str);//Убираем пробелы

        str = scientificRecordNumber(str);//Прячем научную запись числа в переменную

        System.out.println(str);//Выводим безпробельную формулу

        inputVariable(str); //Анализ не введеных переменных

        Result result = PlusMinus(str);
        if(!result.remainder.isEmpty()){
            System.out.println("Error: can't full parse");
            System.err.println("remainder: " + result.remainder);
        }
        return result.accumulator;
    }

    //Работа с сложением и вычитанием //Проверил++
    private Result PlusMinus(String str) throws Exception {

        char ch = 0;
        if(str.charAt(0) == '+' || str.charAt(0) == '-'){
            ch = str.charAt(0);
            str = str.substring(1);
        }

        Result result = MulDiv(str);

        double accumulator;

        if(ch == '-')
            accumulator = -result.accumulator;
        else
            accumulator = result.accumulator;

        while(!result.remainder.isEmpty() && (result.remainder.charAt(0) == '+' || result.remainder.charAt(0) == '-')) {
            char sign = result.remainder.charAt(0); //Получаем знак
            String strWithoutSign = result.remainder.substring(1); // Строка без знака

            result = MulDiv(strWithoutSign);

            if(sign == '+')
                accumulator += result.accumulator;
            else if(sign == '-')
                accumulator -= result.accumulator;

        }
        return new Result(accumulator, result.remainder);
    }

    //Операции умножения и деления // проверил++
    private Result MulDiv(String str) throws Exception {

        Result result = Brackets(str);
        double accumulator = result.accumulator;

        while(!result.remainder.isEmpty() && (result.remainder.charAt(0) == '*' || result.remainder.charAt(0) == '/')) {
            char sign = result.remainder.charAt(0);//Получаем знак
            String strWithoutSign = result.remainder.substring(1);

            result = Brackets(strWithoutSign);

            if(sign == '*')
                accumulator *= result.accumulator;
            else if(sign == '/')
                accumulator /= result.accumulator;
        }

        return new Result(accumulator, result.remainder);
    }

    //Метод обрабатывающий скобки //Проверил++
    private Result Brackets(String str) throws Exception {
        if(!str.isEmpty() && str.charAt(0) == '(' && str.charAt(1)!=')')
        {
            Result result = PlusMinus(str.substring(1));

            if(!result.remainder.isEmpty() && result.remainder.charAt(0) == ')')
                result.remainder = result.remainder.substring(1);
            else
                throw new Exception("The brackets are not closed");

            return result;
        }else
            if(!str.isEmpty() && str.charAt(0) == '(' && str.charAt(1)==')')
                throw new Exception("In parentheses is empty");

        return functionVariable(str);
    }

    //Возвращаем индексы запятых, ктр находятся в скобочках //Проверил++
    private String collectCommas(String str) {
        StringBuilder countComma = new StringBuilder();
        int countBracket = 0;

        for(int i = 0, n = str.length(); i < n; i++){
            if(str.charAt(i) == '(')
                countBracket++;

            if(str.charAt(i) == ')')
                countBracket--;

            if(str.charAt(i) == ',')
                countComma.append(Integer.toString(i)).append(" ");

            if(countBracket == 0){
                if(!countComma.toString().equals(""))
                    countComma.append(Integer.toString(i)).append(" ");
                break;
            }
        }
        return countComma.toString();
    }

    //Вычисляет пользовательские функции и переменные //Проверил++
    private Result functionVariable(String str) throws Exception {

        Pattern pat = Pattern.compile("[A-Z,a-z]+[A-Z,a-z0-9]*");
        Matcher mat = pat.matcher(str);

        // Ищем функцию или переменную
        if(mat.find() && mat.start() == 0){
            Result result = new Result();

            if(!str.substring(mat.end()).isEmpty() && str.charAt(mat.end())== '('){ //Нашли функцию

                String s = collectCommas(str.substring(mat.end()));
                List<Double> doubleList = new ArrayList<>();

                //Если один аргумент функции
                if(s.equals("")){
                    result = Brackets(str.substring(mat.end()));
                    doubleList.add(result.accumulator);
                    result.accumulator = Function(mat.group(), doubleList);
                }
                else{//Несколько аргументов

                    Pattern pat1 = Pattern.compile("[0-9]+");
                    Matcher mat1 = pat1.matcher(s);

                    int startInterval = 0, endInterval = 0;

                    while (mat1.find())
                    {
                        endInterval = Integer.parseInt(mat1.group());
                        doubleList.add(Brackets(str.substring(mat.end() + startInterval + 1, mat.end() + endInterval)).accumulator);
                        startInterval = endInterval;
                    }
                    result.remainder = str.substring(mat.end() + endInterval+1);
                    result.accumulator = Function(mat.group(), doubleList);
                }

            }
            else{ //Нашли переменную
                result.accumulator = Storage.getVariables(mat.group());
                result.remainder = str.substring(mat.end());
            }
            return new Result(result.accumulator, result.remainder);
        }
        return Number(str);
    }

    //Вычисление функций //Проверил++
    private double Function(String nameFunction, List<Double> arrayList) throws Exception {

        nameFunction = nameFunction.toLowerCase();//Переводим название функции в нижний регистр

        double resultMathFunction = 0;

        switch (nameFunction) {
            case "sin":
                resultMathFunction = Math.sin(Math.toRadians(arrayList.get(0)));
                break;
            case "cos":
                resultMathFunction = Math.cos(Math.toRadians(arrayList.get(0)));
                break;
            case "pow":
                resultMathFunction = Math.pow(arrayList.get(0), arrayList.get(1));
                break;
            case "log":
                resultMathFunction = Math.log(arrayList.get(0));
                break;
            case "abs":
                resultMathFunction = Math.abs(arrayList.get(0));
                break;
            case "mod":
                resultMathFunction = arrayList.get(0) % arrayList.get(1);
                break;
            case "sqrt":
                resultMathFunction = Math.sqrt(arrayList.get(0));
                break;
            case "ceil":
                resultMathFunction = Math.ceil(arrayList.get(0));
                break;
            case "floor":
                resultMathFunction = Math.floor(arrayList.get(0));
                break;
            default:{
                String bodyFunction = Storage.getFunctions(nameFunction);

                if(bodyFunction != null) {
                    Pattern pat1, pat2;
                    Matcher mat1, mat2;

                    String newStr = bodyFunction;

                    //Удаляем функции
                    pat1 = Pattern.compile("[A-Za-z]+[A-Za-z0-9]*\\(");
                    mat1 = pat1.matcher(newStr);

                    newStr = mat1.replaceAll("");

                    //Ищем в строке без функций названия переменных
                    pat2 = Pattern.compile("[A-Za-z]+[A-Za-z0-9]*");//Шаблон переменной
                    mat2 = pat2.matcher(newStr);

                    ArrayList<String> mass_str = new ArrayList<>();

                    for(int i = 0; mat2.find(); i++) {
                        if(mass_str.contains(mat2.group())) {
                            bodyFunction = bodyFunction.replaceFirst(mat2.group(), String.valueOf(arrayList.get(mass_str.indexOf(mat2.group()))));
                            continue;
                        }
                        mass_str.add(mat2.group());
                        bodyFunction = bodyFunction.replaceFirst(mat2.group(), String.valueOf(arrayList.get(i)));
                    }
                    resultMathFunction = PlusMinus(bodyFunction).accumulator;
                }
            }
        }
        return resultMathFunction;
    }

    //Проверил++
    private Result Number(String str) throws Exception {

        boolean negative = false;
        double numberPart;
        String remainderPart;
        Pattern pat = Pattern.compile("[0-9]+\\.?[0-9]*");
        Matcher mat;

        //Если число отрицательно
        if(str.charAt(0) == '-') {
            negative = true;
            str = str.substring(1);
        }
        else if(str.charAt(0) == '+')
            str = str.substring(1);

        mat = pat.matcher(str);

        //Ищем подстроку, ктр является числом
        if(mat.find()&& mat.start() == 0){
            numberPart = Double.parseDouble(mat.group());
            remainderPart = str.substring(mat.end());
        }
        else{
            Result res = PlusMinus(str);
            numberPart = res.accumulator;
            remainderPart = res.remainder;
        }

        if(negative)
            numberPart = -numberPart;

        return new Result(numberPart, remainderPart);
    }

    //функция удаления пробелов, в выражении
    private String delSpace(String str) {
        //Убираем пробелы
        Pattern pat = Pattern.compile("\\s");
        String strings[] = pat.split(str);
        StringBuilder newStr = new StringBuilder();

        //Слепляем массив строк, в одну строку
        for (String s: strings)
            newStr.append(s);

        return newStr.toString();
    }

    //Инициализация переменных //Проверил++
    private void inputVariable(String str){

        Pattern pat1, pat2;
        Matcher mat1, mat2;
        String newStr = new String(str);

        //Удаляем функции
        pat1 = Pattern.compile("[A-Za-z]+[A-Za-z0-9]*\\(");
        mat1 = pat1.matcher(newStr);
        newStr = mat1.replaceAll("");

        //Ищем в строке без функций названия переменных
        pat2 = Pattern.compile("[A-Za-z]+[A-Za-z0-9]*");//Шаблон переменной
        mat2 = pat2.matcher(newStr);
        Scanner sc = new Scanner(System.in);

        while(mat2.find()){
            String varName = mat2.group();
            if(Storage.getVariables(varName) == Double.MIN_VALUE) {
                System.out.println("Input variable " + varName + ": ");
                Storage.setVariables(varName, Double.parseDouble(sc.next()));
            }
        }
    }

    //Парсим научную запись числа //Проверил++
    private String scientificRecordNumber(String str){

        Pattern pat = Pattern.compile("[0-9]+\\.?[0-9]*[Ee]+?[\\+\\-]?[0-9]+");
        Matcher mat = pat.matcher(str);

        while(mat.find()) {
            double parseDouble = Double.parseDouble(mat.group());
            String nameVar = Util.getRandomString(4);

            //Заносим в hashmap переменную и значение числа
            Storage.setVariables(nameVar, parseDouble);

            //Заменяем научную запись числа на случайную строку
            str = mat.replaceFirst(nameVar);
            mat = pat.matcher(str);
        }
        return str;
    }
}














