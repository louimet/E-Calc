package com.calc;

/**
 * Created by Yuanwen on 2016/3/28.
 */
public class Memory {
    private static double lastAnswer = 0.0;
    private static double memoryBuffer = 0.0;

    public static double getLastAnswer(){
        return lastAnswer;
    }

    public static void setLastAnswer(double newAnswer){
        lastAnswer= newAnswer;
    }

    public static double getMemoryBuffer(){
        return memoryBuffer;
    }

    public static void setMemoryBuffer(double newAnswer){
        memoryBuffer = newAnswer;
    }
}
