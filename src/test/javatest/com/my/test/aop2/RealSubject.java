package com.my.test.aop2;

/**
 * Created by Administrator on 2018-06-20.
 */
public class RealSubject implements Subject{
    public void request(){
        System.out.println("From real subject.");
    }
}
