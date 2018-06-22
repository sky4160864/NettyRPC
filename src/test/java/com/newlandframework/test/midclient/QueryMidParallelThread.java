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
package com.newlandframework.test.midclient;

import com.newlandframework.rpc.exception.InvokeTimeoutException;
import com.newlandframework.rpc.services.ReqMiddleDatasManage;
import com.newlandframework.rpc.services.pojo.ReqMiddleDatas;
import com.newlandframework.rpc.services.pojo.ResMiddleDatas;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author tangjie<https://github.com/tang-jie>
 * @filename:AddCalcParallelRequestThread.java
 * @description:AddCalcParallelRequestThread功能模块
 * @blogs http://www.cnblogs.com/jietang/
 * @since 2016/10/7
 */
public class QueryMidParallelThread implements Runnable {
    private org.slf4j.Logger logger = LoggerFactory.getLogger(QueryMidParallelThread.class);

    private CountDownLatch signal;
    private CountDownLatch finish;
    private int taskNumber = 0;
    private ReqMiddleDatasManage manage;
    private ReqMiddleDatas qryDatas;

    public QueryMidParallelThread(ReqMiddleDatasManage manage, ReqMiddleDatas qryDatas, CountDownLatch signal, CountDownLatch finish, int taskNumber) {
        this.signal = signal;
        this.finish = finish;
        this.taskNumber = taskNumber;
        this.manage = manage;
        this.qryDatas = qryDatas;
    }

    public void run() {
        try {
            signal.await();
            long btime = System.currentTimeMillis();
            ResMiddleDatas rlt = manage.query(qryDatas);
            logger.info("ST:{} TP:{} MN:{} RP:{} Bwtime:{} Result:{} Size:{} Cost:{}", rlt.getReq().getSt(), rlt.getReq().getTp(), rlt.getReq().getMn1(), rlt.getReq().getMn2(), rlt.getReq().getBtime() + "-" + rlt.getReq().getEtime(), rlt.getResult(), rlt.getList().size(), System.currentTimeMillis() - btime);

        } catch (InterruptedException ex) {
            Logger.getLogger(QueryMidParallelThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvokeTimeoutException ex) {
            System.out.println(ex.getMessage());
        } finally {
            finish.countDown();
        }
    }
}

