package com.newlandframework.test;

import org.junit.Test;

/**
 * Created by Huang Jianhai on 2018/6/19.
 */
public class MyTest {
    @Test
    public void sp(){
        String[] ss = "".split(",");
        for(String s:ss){
            System.out.println(s);
        }
        System.out.println("--------"+ss.length);
    }
}
