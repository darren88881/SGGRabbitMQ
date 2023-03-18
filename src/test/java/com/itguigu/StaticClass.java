package com.itguigu;

/**
 *
 * @Author darren
 * @Date 2023/3/18 11:37
 */
public class StaticClass {
    public static void main(String[] args) {
        int a = D.a;
        System.out.println(a);
    }
}

class D{
    static int a = 0;
    static{
        a = 10;
        System.out.println("D的静态代码块被调用");
    }
    public static int getn1(){
        System.out.println("getn1被调用");
        return 100;
    }
    public int getn2(){
        System.out.println("getn2被调用");
        return 99;
    }
}