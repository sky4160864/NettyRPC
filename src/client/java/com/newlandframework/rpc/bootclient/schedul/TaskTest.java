package com.newlandframework.rpc.bootclient.schedul;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Huang Jianhai on 2018/7/16.
 */
/*@Service
@PropertySource(value = "file:${user.dir}/rpc-client.properties")
public class TaskTest {
    private Logger logger = LoggerFactory.getLogger(TaskTest.class);
    @Scheduled(cron = "${job1_expression}") // 每隔1秒隔行一次
    //@Scheduled(cron = "0/1 * * * * ? ")
    public void run() throws InterruptedException {
        logger.info("{}",new Date());
        TimeUnit.MILLISECONDS.sleep(5000);
    }
}*/
