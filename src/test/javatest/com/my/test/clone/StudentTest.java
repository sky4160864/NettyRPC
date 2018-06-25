package com.my.test.clone;

import org.junit.Test;
import org.springframework.beans.BeanUtils;

/**
 * Created by Huang Jianhai on 2018/6/25.
 */
public class StudentTest {

    @Test
    public void main() {
        //浅拷贝
        Student stu1 = new Student();
        stu1.setNumber(12345);
        Student stu2 = (Student)stu1.clone();

        System.out.println("学生1:" + stu1.getNumber());
        System.out.println("学生2:" + stu2.getNumber());

        stu2.setNumber(54321);

        System.out.println("学生1:" + stu1.getNumber());
        System.out.println("学生2:" + stu2.getNumber());

        System.out.println(stu1 == stu2);
    }

    @Test
    public void test2(){
        //未现实在StudentDeep拷贝时对Address复制，也是浅拷贝
        Address addr = new Address();
        addr.setAdd("杭州市");
        Student stu1 = new Student();
        stu1.setNumber(123);
        stu1.setAddr(addr);
        Student stu2 = (Student)stu1.clone();
        System.out.println("学生1:" + stu1.getNumber() + ",地址:" + stu1.getAddr().getAdd());
        System.out.println("学生2:" + stu2.getNumber() + ",地址:" + stu2.getAddr().getAdd());

        addr.setAdd("西湖区");
        System.out.println("学生1:" + stu1.getNumber() + ",地址:" + stu1.getAddr().getAdd());
        System.out.println("学生2:" + stu2.getNumber() + ",地址:" + stu2.getAddr().getAdd());
    }


    @Test
    public void test3(){
        //深度拷贝：需要对Address现实Cloneable  且在StudentDeep拷贝时，对Address复制
        Address addr = new Address();
        addr.setAdd("杭州市");
        StudentDeep stu1 = new StudentDeep();
        stu1.setNumber(123);
        stu1.setAddr(addr);
        StudentDeep stu2 = (StudentDeep)stu1.clone();
        System.out.println("学生1:" + stu1.getNumber() + ",地址:" + stu1.getAddr().getAdd());
        System.out.println("学生2:" + stu2.getNumber() + ",地址:" + stu2.getAddr().getAdd());

        addr.setAdd("西湖区");
        System.out.println("学生1:" + stu1.getNumber() + ",地址:" + stu1.getAddr().getAdd());
        System.out.println("学生2:" + stu2.getNumber() + ",地址:" + stu2.getAddr().getAdd());
    }

    @Test
    public void test4(){
        //BeanUtils
        Address addr = new Address();
        addr.setAdd("杭州市");
        Student stu1 = new Student();
        stu1.setNumber(123);
        stu1.setAddr(addr);
        Student stu2= new Student();
        StudentDeep stu3= new StudentDeep();
        BeanUtils.copyProperties(stu1,stu2); //BeanUtils
        BeanUtils.copyProperties(stu1,stu3,"number");
        System.out.println("学生1:" + stu1.getNumber() + ",地址:" + stu1.getAddr().getAdd());
        System.out.println("学生2:" + stu2.getNumber() + ",地址:" + stu2.getAddr().getAdd());
        System.out.println("学生3:" + stu3.getNumber() + ",地址:" + stu3.getAddr().getAdd());
        addr.setAdd("西湖区");
        System.out.println("学生1:" + stu1.getNumber() + ",地址:" + stu1.getAddr().getAdd());
        System.out.println("学生2:" + stu2.getNumber() + ",地址:" + stu2.getAddr().getAdd());
        System.out.println("学生3:" + stu3.getNumber() + ",地址:" + stu3.getAddr().getAdd());

    }
}
