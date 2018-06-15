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
import java.util.List;

/**
 * @author tangjie<https://github.com/tang-jie>
 * @filename:Person.java
 * @description:Person功能模块
 * @blogs http://www.cnblogs.com/jietang/
 * @since 2016/11/7
 */
public class ResMiddleDatas implements Serializable {

    private int result ; //1正常返回 -1请求无效
    private ReqMiddleDatas req ;
    private List<MiddleData> list;
    private String compareRLT ;

    public ResMiddleDatas(int result, ReqMiddleDatas req, List<MiddleData> list) {
        this.result = result;
        this.req = req;
        this.list = list;
    }

    public ResMiddleDatas(int result, ReqMiddleDatas req, List<MiddleData> list, String compareRLT) {
        this.result = result;
        this.req = req;
        this.list = list;
        this.compareRLT = compareRLT;
    }

    public String getCompareRLT() {
        return compareRLT;
    }

    public void setCompareRLT(String compareRLT) {
        this.compareRLT = compareRLT;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public ReqMiddleDatas getReq() {
        return req;
    }

    public void setReq(ReqMiddleDatas req) {
        this.req = req;
    }

    public List<MiddleData> getList() {
        return list;
    }

    public void setList(List<MiddleData> list) {
        this.list = list;
    }
}

