/*
package com.newlandframework.rpc.bootclient.schedul;


import com.newlandframework.rpc.bootclient.RpcClientStart;
import com.newlandframework.rpc.bootclient.services.ReqDatasManage;
import com.newlandframework.rpc.exception.InvokeTimeoutException;
import com.newlandframework.rpc.netty.RpcServerLoader;
import com.newlandframework.rpc.services.ReqMiddleDatasManage;
import com.newlandframework.rpc.spring.PropertyPlaceholder;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

*/
/**
 * 实际执行任务的业务类,需要实现Job接口
 * @remark
 *//*

public class JobTestWhileIdle implements Job {
    private static Logger logger = LoggerFactory.getLogger(JobTestWhileIdle.class);
    */
/**
     * 执行任务的方法
     *//*

    public void execute(JobExecutionContext context) throws JobExecutionException {
        if(!RpcServerLoader.getInstance().getMessageSendStatus()){
            logger.info("==========NOT CONNECT SERVER==========");
            return;
        }

        ReqMiddleDatasManage reqManage = (ReqMiddleDatasManage) RpcClientStart.context.getBean("midDatasService");
        try{
            reqManage.testWhileIdle("##testWhileIdle##");
        }catch (InvokeTimeoutException e) {
            logger.error("[InvokeTimeoutException]");
        }
    }
}
*/
