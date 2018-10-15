package com.my.test;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * Created by Huang Jianhai on 2018/6/27.
 */
public class NanoTest {
    @Test
    public void ss() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            long start = System.nanoTime();
            TimeUnit.MICROSECONDS.sleep(1000000);
            long end = System.nanoTime();
            System.out.println(end-start);
        }
    }

    @Test
    public void mathlog(){
        System.out.println(Math.round(Math.floor(Math.log10(99000))));
    }

    @Test
    public void te(){
        String ss="version";
        System.out.println(ss.startsWith("version"));
    }
}
