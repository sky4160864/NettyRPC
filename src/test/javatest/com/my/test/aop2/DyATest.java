package com.my.test.aop2;

import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * Created by Huang Jianhai on 2018/6/25.
 */
public class DyATest {
    @Test
    public void tt(){
        DyA a = new DyAImpl();
        InvocationHandler handler = new DynamicA(a);
        Class clz = a.getClass();
        DyA da = (DyA)Proxy.newProxyInstance(clz.getClassLoader(),clz.getInterfaces(),handler);
        da.prt("dssdsd");
    }
}
