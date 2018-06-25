package com.my.test.clone;

/**
 * Created by Huang Jianhai on 2018/6/25.
 */
public class StudentDeep implements Cloneable{
    private int number;

    private Address addr;

    public Address getAddr() {
        return addr;
    }

    public void setAddr(Address addr) {
        this.addr = addr;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public Object clone() {
        StudentDeep stu = null;
        try{
            stu = (StudentDeep)super.clone();
            stu.setAddr((Address)addr.clone());   //深度复制
        }catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return stu;
    }
}
