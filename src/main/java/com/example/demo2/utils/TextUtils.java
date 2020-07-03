package com.example.demo2.utils;

public class TextUtils {

    public static boolean isEmpty(String text){
        return text == null || text.length() == 0;
    }

    public static boolean isLegal(String password){
        if(password.length() >= 6){
            return true;
        }
        return false;
    }

    public static boolean Grade100IsLeagal(int grade100){
        if(0 <= grade100 && grade100 <= 100 ){
            return true;
        }
        return false;
    }

    public static boolean Grade5IsLeagal(double grade100){
        if(0 <= grade100 && grade100 <= 5.0 ){
            return true;
        }
        return false;
    }
}
