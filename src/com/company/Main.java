package com.company;

import com.company.Classes.AnalysisOfTheFormula;
import com.company.Classes.Storage;
import java.util.Scanner;


public class Main {

    public static void main(String[] args){

        Scanner sc = new Scanner(System.in);
        String mainMenu = "Калькулятор поддерживает:\n" +
                "- операторы +,-,*,/,()\n" +
                "- математические функции - sin, cos, pow, log, abs, mod, sqrt, ceil, floor\n" +
                "- переменные (var x = 12)\n" +
                "- определение новых функций (func f = 1 + x)\n" +
                "- научную запись числа (1.256E+2)\n" +
                "Если вы хотите использовать пользовательские функции, " +
                "то их нужно в начале объявить и только затем использовать в выражении.\n" +
                "Если хотите выйти, введите слово 'stop'.\n" +
                "Введите выражение, которое хотите вычислить.\n";

        System.out.println(mainMenu);
        String inputString = sc.nextLine();

        while (!inputString.equals("stop")) {
            String input_str = Storage.varFunc(inputString);//вводим переменные и функции

            //String input_str ="-(4e0+4)* x + (sqrt(9) + (5e-1 + 3*2))  +mod(x,3)";
            //String input_str ="f(2,3) + x";

            AnalysisOfTheFormula aof = new AnalysisOfTheFormula();

            try {
                System.out.println("result: " + aof.calculate(input_str));
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(mainMenu);
            inputString = sc.nextLine();
        }
    }
}
