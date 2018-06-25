package com.my.test.clone;

/**
 * Created by Huang Jianhai on 2018/6/25.
 */
class Address  implements Cloneable{
    private String add;
    public String getAdd() {
        return add;
    }
    public void setAdd(String add) {
        this.add = add;
    }

    @Override
    public Object clone() {
        Address adr = null;
        try {
            adr = (Address)super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }finally {
            return adr;
        }
    }

}
