package com.newlandframework.rpc.bootclient.schedul;

/**
 * Created by Huang Jianhai on 2018/6/15.
 */

import com.newlandframework.rpc.bootclient.RpcClientStart;
import com.newlandframework.rpc.bootclient.services.ReqDatasManage;
import com.newlandframework.rpc.services.ReqMiddleDatasManage;
import com.newlandframework.rpc.services.pojo.ReqMiddleDatas;
import com.newlandframework.rpc.services.pojo.ResMiddleDatas;
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
public class JobOne implements Job {
    private Logger logger = LoggerFactory.getLogger(JobOne.class);
    /**
     * 执行任务的方法
     */
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("==========request datas start ==========");
        long btime = System.currentTimeMillis();
        ReqMiddleDatasManage reqManage = (ReqMiddleDatasManage) RpcClientStart.context.getBean("midDatasService");
        ReqDatasManage req = (ReqDatasManage)RpcClientStart.context.getBean("ReqDatasManage");
        List<ReqMiddleDatas> list = req.query01();
        for (int i = 0; i < list.size(); i++) {
            ResMiddleDatas rlt = reqManage.query(list.get(i));

            if(rlt.getResult()==1){
                int size = rlt.getList()==null?0:rlt.getList().size();
                if(size>0){
                    req.save(rlt);
                }
                logger.info("{}: result:{} size:{} time:{}",rlt.getReq().getMn1(),rlt.getResult(),size,System.currentTimeMillis()-btime);

            }else{
                logger.error("[RES ERR]{}",rlt.getReq());
            }
        }
        logger.info("==========request datas end==========");

    }
}
