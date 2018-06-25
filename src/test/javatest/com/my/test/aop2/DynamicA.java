package com.my.test.aop2;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by Huang Jianhai on 2018/6/25.
 */
public class DynamicA implements InvocationHandler {
    private Object ob;
    public DynamicA(Object ob){
        this.ob = ob ;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("1111111111");
        method.invoke(ob, args);
        System.out.println("2222222222");
        return null;
    }
}
