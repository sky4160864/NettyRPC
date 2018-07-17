/*
package com.newlandframework.rpc.bootclient.schedul;

*/
/**
 * Created by Huang Jianhai on 2018/6/15.
 *//*


import com.newlandframework.rpc.bootclient.RpcClientStart;
import com.newlandframework.rpc.bootclient.services.ReqDatasManage;
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

public class JobOne implements Job {
    private Logger logger = LoggerFactory.getLogger(JobOne.class);
    //private boolean running = false;
    */
/**
     * 执行任务的方法
     *//*

    public void execute(JobExecutionContext context) throws JobExecutionException {
        try{
            if(QuartzUtil.running){
                logger.info("==========running==========");
                return;
            }else{
                QuartzUtil.running = true;
            }
            if(!RpcServerLoader.getInstance().getMessageSendStatus()){
                logger.info("==========NOT CONNECT SERVER==========");
                return;
            }
            logger.info("==========request datas start==========");
            String fetchWS = PropertyPlaceholder.getProperty("need_fetch_ws","1");
            long btime = System.currentTimeMillis();
            ReqMiddleDatasManage reqManage = (ReqMiddleDatasManage) RpcClientStart.context.getBean("midDatasService");
            ReqDatasManage req = (ReqDatasManage)RpcClientStart.context.getBean("ReqDatasManage");
            logger.info("=====01=====");
            req.doReqManage(req.query01(),reqManage);
            if("1".equals(fetchWS)) {
                logger.info("=====09=====");
                req.doReqManage(req.query09(), reqManage);
            }
            logger.info("=====21=====");
            req.doReqManage(req.query21(),reqManage);
            logger.info("==========request datas end {}==========",System.currentTimeMillis()-btime);
        }finally {
            QuartzUtil.running = false;
        }

    }
}
*/
