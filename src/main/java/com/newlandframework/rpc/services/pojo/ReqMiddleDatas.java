/**
 * Copyright (C) 2016 Newland Group Holding Limited
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.newlandframework.rpc.services.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author tangjie<https://github.com/tang-jie>
 * @filename:Person.java
 * @description:Person功能模块
 * @blogs http://www.cnblogs.com/jietang/
 * @since 2016/11/7
 */
public class ReqMiddleDatas implements Serializable {
    private String st; //
    private String tp; // 1水  2气
    private String mn1;
    private String mn2;
    private String btime;
    private String etime;
    private int nums;

    public int getNums() {
        return nums;
    }

    public void setNums(int nums) {
        this.nums = nums;
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public String getTp() {
        return tp;
    }

    public void setTp(String tp) {
        this.tp = tp;
    }

    public String getMn1() {
        return mn1;
    }

    public void setMn1(String mn1) {
        this.mn1 = mn1;
    }

    public String getMn2() {
        return mn2;
    }

    public void setMn2(String mn2) {
        this.mn2 = mn2;
    }

    public String getBtime() {
        return btime;
    }

    public void setBtime(String btime) {
        this.btime = btime;
    }

    public String getEtime() {
        return etime;
    }

    public void setEtime(String etime) {
        this.etime = etime;
    }

    @Override
    public String toString() {
        return String.format("[ST:%s TP:%s MN1:%s MN2:%s BTIME:%s ETIME:%s]", st, tp, mn1, mn2,btime,etime);
    }
}

