package com.newlandframework.rpc.bootclient.schedul;

import com.newlandframework.rpc.bootclient.RpcClientStart;
import com.newlandframework.rpc.bootclient.services.ReqDatasManage;
import com.newlandframework.rpc.netty.RpcServerLoader;
import com.newlandframework.rpc.services.ReqMiddleDatasManage;
import com.newlandframework.rpc.spring.PropertyPlaceholder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Created by Huang Jianhai on 2018/7/16.
 */
@Service
@PropertySource(value = "file:${user.dir}/rpc-client.properties")
public class Task1 {
    private Logger logger = LoggerFactory.getLogger(Task1.class);

    @Scheduled(cron = "${job1_expression}")
    public void run(){
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
    }
}
