package com.newlandframework.rpc.bootclient.schedul;


import com.newlandframework.rpc.bootclient.RpcClientStart;
import com.newlandframework.rpc.bootclient.services.ReqDatasManage;
import com.newlandframework.rpc.netty.RpcServerLoader;
import com.newlandframework.rpc.services.ReqMiddleDatasManage;
import com.newlandframework.rpc.services.pojo.ReqMiddleDatas;
import com.newlandframework.rpc.services.pojo.ResMiddleDatas;
import com.newlandframework.rpc.spring.PropertyPlaceholder;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 实际执行任务的业务类,需要实现Job接口
 * @remark
 */
public class JobTwo implements Job {
    private static Logger logger = LoggerFactory.getLogger(JobTwo.class);
    /**
     * 执行任务的方法
     */
    public void execute(JobExecutionContext context) throws JobExecutionException {
        if(!RpcServerLoader.getInstance().getMessageSendStatus()){
            logger.info("==========NOT CONNECT SERVER==========");
            return;
        }
        logger.info("==========compare datas start ==========");
        String fetchWS = PropertyPlaceholder.getProperty("need_fetch_ws","1");
        long btime = System.currentTimeMillis();
        ReqMiddleDatasManage reqManage = (ReqMiddleDatasManage) RpcClientStart.context.getBean("midDatasService");
        ReqDatasManage req = (ReqDatasManage)RpcClientStart.context.getBean("ReqDatasManage");
        logger.info("=====cp11=====");
        req.doReqManage(req.query11(),reqManage);
        if("1".equals(fetchWS)){
            logger.info("=====cp19=====");
            req.doReqManage(req.query19(),reqManage);
        }

        logger.info("==========compare datas end {}==========",System.currentTimeMillis()-btime);
    }
}
