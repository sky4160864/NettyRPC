package com.newlandframework.test.midclient;

import com.newlandframework.rpc.bootclient.RpcClientStart;
import com.newlandframework.rpc.bootclient.services.ReqDatasManage;
import com.newlandframework.rpc.services.AddCalculate;
import com.newlandframework.rpc.services.MultiCalculate;
import com.newlandframework.rpc.services.ReqMiddleDatasManage;
import com.newlandframework.rpc.services.pojo.ReqMiddleDatas;
import com.newlandframework.test.AddCalcParallelRequestThread;
import com.newlandframework.test.RpcParallelTest;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by Huang Jianhai on 2018/6/22.
 */
public class MidClient {

    public static void parallelQueryTask(ReqMiddleDatasManage manage,List<ReqMiddleDatas> list) throws InterruptedException {
        //开始计时
        StopWatch sw = new StopWatch();
        sw.start();

        CountDownLatch signal = new CountDownLatch(1);
        CountDownLatch finish = new CountDownLatch(list.size());

        for (int index = 0; index < list.size(); index++) {
            QueryMidParallelThread client = new QueryMidParallelThread(manage, list.get(index),signal, finish, index);
            new Thread(client).start();
        }
        signal.countDown();
        finish.await();
        sw.stop();

        String tip = String.format("请求 RPC调用总共耗时: [%s] 毫秒", sw.getTime());
        System.out.println(tip);
    }

    public static void qryTask(ReqMiddleDatasManage manage, List<ReqMiddleDatas> list) throws InterruptedException {
        parallelQueryTask(manage, list);
        TimeUnit.MILLISECONDS.sleep(30);
    }

    public static void main(String[] args) throws Exception {
        //并行度1000
        int parallel = 1000;
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-rpc-client-config.xml");

        for (int i = 0; i < 1; i++) {
            ReqMiddleDatasManage reqManage = (ReqMiddleDatasManage) context.getBean("midDatasService");
            ReqDatasManage req = (ReqDatasManage) context.getBean("ReqDatasManage");
            qryTask(reqManage,req.query01());
            System.out.printf("[author tangjie] Netty RPC Server 消息协议序列化第[%d]轮并发验证结束!\n\n", i);
        }

        context.destroy();
    }
}
