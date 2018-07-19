package com.newlandframework.test.spring.schema;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.TimeUnit;

public class TestSpringSchema {

    @SuppressWarnings("resource")
    @Test
    public void test() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-shcema-user.xml");

        User user = (User) context.getBean("eric");
        System.out.println(user.getId());
        System.out.println(user.getName());
        System.out.println(user.getSex());
        System.out.println(user.getAge());
        user.setAge(88);
        user = (User) context.getBean("eric");
        System.out.println(user.getId());
        System.out.println(user.getName());
        System.out.println(user.getSex());
        System.out.println(user.getAge());
    }

    @Test
    public void test2(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-shcema-user.xml");
        User user1 = (User) context.getBean("eric");
        System.out.println(user1.getAge());
        new Thread(()->{
            User user = (User) context.getBean("eric");
            try {
                TimeUnit.MILLISECONDS.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            user.setAge(88);
        }).start();

        User user = (User) context.getBean("eric");
        System.out.println(user.getAge());

        try {
            TimeUnit.MILLISECONDS.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        user = (User) context.getBean("eric");
        System.out.println(user.getAge());
    }
}
