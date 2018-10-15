package com.newlandframework.rpc.hac;

import com.newlandframework.rpc.boot.RpcServerStarter2;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;


/**
 * 实际执行任务的业务类,需要实现Job接口
 * @remark
 */
public class JobOne implements Job {
    private Logger logger = LoggerFactory.getLogger(JobOne.class);
    //private boolean running = false;

    /**
     * 执行任务的方法
     */
    public void execute(JobExecutionContext context) throws JobExecutionException {
        RpcServerStarter2.destroy();
        VersionCtl.versionMap.clear();
        try {
            TimeUnit.MINUTES.sleep(10L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        RpcServerStarter2.init();
    }
}

