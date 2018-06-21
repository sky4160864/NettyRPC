package com.newlandframework.test.quartz;

/**
 * Created by Huang Jianhai on 2018/6/15.
 */

import com.newlandframework.rpc.bootclient.schedul.QuartzUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;

/**
 * 实际执行任务的业务类,需要实现Job接口
 * @remark
 */
public class JobTest implements Job {
    private Logger logger = LoggerFactory.getLogger(JobTest.class);
    /**
     * 执行任务的方法
     */
    public void execute(JobExecutionContext context) throws JobExecutionException {
        if(QuartzTest.running){
            logger.info("==========running==========");
            return;
        }else{
            QuartzTest.running = true;
        }

        try {
            logger.info("==========doing==========");
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        QuartzTest.running = false;
    }
}
