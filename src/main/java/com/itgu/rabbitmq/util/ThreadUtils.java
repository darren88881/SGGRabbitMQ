package com.itgu.rabbitmq.util;

/**
 * @author darren
 * @create 2021-11-13 14:42
 */
public class ThreadUtils {

    public static void sleep(int second){
        try {
            Thread.sleep(1000*second);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
