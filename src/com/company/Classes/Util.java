package com.company.Classes;

import java.util.Random;

public class Util {

    public static String getRandomString(int strLength) {

        //Генерируем название переменной ктр будет хранить научную запись числа
        String dict = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        Random rnd = new Random();
        StringBuilder randomStr = new StringBuilder();

        for(int i = 0; i < strLength; i++){
            int index = rnd.nextInt(dict.length());
            randomStr.append(dict.charAt(index));
        }
        return randomStr.toString();
    }
}
