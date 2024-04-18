package com.yyf.rabbitmq.util;

/**
 * @Author: yyf
 * @Date: 2023/11/22/下午9:42
 * @Description:
 */
public class SleepUtils {
    public static void sleep(int second){
        try {
            Thread.sleep(1000*second);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
