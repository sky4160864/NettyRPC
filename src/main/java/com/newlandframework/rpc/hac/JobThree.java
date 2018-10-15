package com.newlandframework.rpc.hac;


import com.alibaba.druid.pool.DruidDataSource;
import com.newlandframework.rpc.boot.RpcServerStarter2;
import com.newlandframework.rpc.services.ReqMiddleDatasManage;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 实际执行任务的业务类,需要实现Job接口
 * @remark
 */
public class JobThree implements Job {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 执行任务的方法
     */
    public void execute(JobExecutionContext context) throws JobExecutionException {

        /*DruidDataSource dataSource = (DruidDataSource)RpcServerStarter2.context.getBean("dataSource");
        logger.info("[Druid] ActiveCount:{}, ConnectCount:{}, ConnectErrorCount:{}, CloseCount:{}",
                dataSource.getActiveCount(),dataSource.getConnectCount(),
                dataSource.getConnectErrorCount(),dataSource.getCloseCount());*/


        ReqMiddleDatasManage reqManage = (ReqMiddleDatasManage) RpcServerStarter2.context.getBean("midDatasServiceImpl");
        for (int i = 0; i <20 ; i++) {
            //logger.info("testWhileIdle");
            reqManage.testWhileIdle();
        }

    }
}

