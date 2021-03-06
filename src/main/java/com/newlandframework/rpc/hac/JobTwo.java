package com.newlandframework.rpc.hac;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 实际执行任务的业务类,需要实现Job接口
 * @remark
 */
public class JobTwo implements Job {
    private Logger logger = LoggerFactory.getLogger(JobTwo.class);
    /**
     * 执行任务的方法
     */
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("[Memory]{}", RuntimeUtil.getMemory("hacMidServer.exe"));
    }
}

