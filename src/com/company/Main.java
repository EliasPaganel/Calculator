package com.company;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args){

        Pattern pat;
        Matcher mat;
        String input_str [] ={"-17 +       (  11 /     5.5    + (4-5)*4.0)*2"} ;






/*
        String input_str = "17+(4.3-5.2377)*3.2 + , 3.22/ ! 5.123";

        Pattern pat = Pattern.compile("([0-9]+\\.?[0-9]*)");
        //Pattern pat = Pattern.compile("[-+/*()]");
        Matcher mat = pat.matcher(input_str);

        while(mat.find()) {
            System.out.print(mat.group() + " ");
            System.out.print(mat.end() + " ");

            //System.out.print(mat.start());
            System.out.print("    ");
        }
*/

/*
        Pattern pat = Pattern.compile("W+");
        Matcher mat = pat.matcher("W WW WWW") ;
        while(mat.find())
        System.out.println("Coвпaдeниe: " + mat.group()) ;*/

/*
        Pattern pat = Pattern.compile("[ ,.!]");
        String strs [] = pat.split("one two,alpha9 12!done.");
        for(String s: strs) System.out.println("Следующая лексема: "+s);

/*
        Pattern pat = Pattern.compile("John.*? ");
        Matcher mat = pat.matcher("John Johnathan Frank Ken Todd");

        System.out.println("Измененная последовательность: " + mat.replaceAll("ERIC "));*/
/*
        //Разовое сравнение
        System.out.println(Pattern.matches("Becon .*?","Becon can say you, what you bullshit"));

        //Разовое сравнение
        String str = "Becon can say you, what you bullshit";
        System.out.println(str.matches("Becon .*?"));*/






    }
}
