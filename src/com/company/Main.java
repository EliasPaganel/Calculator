package com.company;

import com.company.Classes.Analysis_of_formulas;
import com.company.Classes.Storage;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args){

        Scanner sc = new Scanner(System.in);

        String s = sc.nextLine();

        while (!s.equals("+"))
        {
            String input_str = var_func(s);//вводим переменные и функции

            //String input_str ="-(4e0+4)* x + (sqrt(9) + (5e-1 + 3*2))  +mod(x,3)";
            //String input_str ="f(2,3) + x";

            Analysis_of_formulas aof = new Analysis_of_formulas();

            try {
                System.out.println("result: " + aof.Parse(input_str));
            } catch (Exception e) {
                e.printStackTrace();
            }
            s = sc.nextLine();
        }
    }

    private static String var_func(String s)
    {
        Scanner sc = new Scanner(System.in);
        while (Pattern.matches("var.+",s) || Pattern.matches("func.+",s))
        {
            if(Pattern.matches("var.+",s))
            {
                Pattern pat = Pattern.compile("var|=.+|\\s");
                Matcher mat = pat.matcher(s);
                String first = mat.replaceAll("");

                Pattern pat2 = Pattern.compile(".+=|\\s");
                Matcher mat2 = pat2.matcher(s);
                String second = mat2.replaceAll("");
                if(!first.equals("") && !second.equals(""))
                    Storage.setVariables(first,Double.parseDouble(second));

            }
            else if(Pattern.matches("func.+",s))
            {
                Pattern pat = Pattern.compile("func|=.+|\\s");
                Matcher mat = pat.matcher(s);
                String first = mat.replaceAll("");

                Pattern pat2 = Pattern.compile(".+=|\\s");
                Matcher mat2 = pat2.matcher(s);
                String second = mat2.replaceAll("");
                if(!first.equals("") && !second.equals(""))
                    Storage.setFunctions(first,second);
            }
            s =  sc.nextLine();
        }
        return s;
    }
}
