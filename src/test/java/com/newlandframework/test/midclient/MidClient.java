package com.newlandframework.test.midclient;

import com.newlandframework.rpc.services.ReqMiddleDatasManage;
import com.newlandframework.rpc.services.pojo.ReqMiddleDatas;
import com.newlandframework.rpc.spring.PropertyPlaceholder;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by Huang Jianhai on 2018/6/22.
 */
public class MidClient {
    static long[] recordSW = new long[10];
    static int recordSWIndex = 0;
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(MidClient.class);

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
        logger.info(tip);
        recordSW[recordSWIndex]=sw.getTime();
        recordSWIndex++;
    }

    public static void qryTask(ReqMiddleDatasManage manage, List<ReqMiddleDatas> list) throws InterruptedException {
        parallelQueryTask(manage, list);
        TimeUnit.MILLISECONDS.sleep(30);
    }

    public static void main(String[] args) throws Exception {
       new MidClient().mainTEst();
    }

    @Test
    public void mainTEst() throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-rpc-client-config-concurrent.xml");
        //并行度1000
        int parallel = Integer.parseInt(PropertyPlaceholder.getProperty("parallel","10"));
        logger.info("Netty RPC Server 并行度{}",parallel);


        List<ReqMiddleDatas> list = InitList.getList(parallel);


        for (int i = 0; i < 10; i++) {
            ReqMiddleDatasManage reqManage = (ReqMiddleDatasManage) context.getBean("midDatasService");
            //ReqDatasManage req = (ReqDatasManage) context.getBean("ReqDatasManage");
            qryTask(reqManage,list);
            logger.info("Netty RPC Server 消息协议序列化第{}轮并发验证结束!\n\n", i);
        }

        String rss = "每轮调用耗时：";
        for(long l:recordSW){
            rss = rss +l+",";
        }
        logger.info(rss);

        context.destroy();
    }
}
