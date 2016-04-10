package com.teamE.calc;

/**
* Created by Yuanwen on 2016/4/1.
 * This class is part of the model part of the MVC, it contains latest evaluation result
*/
public class ResultBuffer {
    private static double result = 0;

    public static double getResult(){
        return result;
    }

    public static void setResult(double newResult){
        result = newResult;
    }

    public static void clear(){
        result = 0;
    }
}

