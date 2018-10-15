/**
 * Copyright (C) 2016 Newland Group Holding Limited
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.newlandframework.rpc.boot;

import com.newlandframework.rpc.bootclient.schedul.Task1;
import com.newlandframework.rpc.hac.*;
import com.newlandframework.rpc.netty.RpcServerLoader;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author tangjie<https://github.com/tang-jie>
 * @filename:RpcServerStarter.java
 * @description:RpcServerStarter功能模块
 * @blogs http://www.cnblogs.com/jietang/
 * @since 2016/10/7
 */
public class RpcServerStarter2 {
    private static Logger logger = LoggerFactory.getLogger(RpcServerStarter2.class);
    public static ClassPathXmlApplicationContext context;

    public static void main(String[] args){
        if(!Bootinit.init()){
            return;
        }
        init();
        ScheduleUtil.addJob("job2", "trigger2", JobTwo.class,"0 * * * * ?");
        ScheduleUtil.addJob("job3", "trigger3", JobThree.class,"0/30 * * * * ?");
        //ScheduleUtil.addJob("job1", "trigger1", JobOne.class,"0 0 * * * ?");
    }

    public static void init(){
        context = new ClassPathXmlApplicationContext("classpath:srping-rpc-invoke-config-server.xml");
        logger.info("Server init");
    }

    public static void  destroy(){
        context.destroy();
        logger.info("Server destroy");
    }

   /* class Task1 implements Job{
        public void execute(JobExecutionContext context) throws JobExecutionException {
            logger.info("[Memory]{}", RuntimeUtil.getMemory("hacMidServer.exe"));
        }
    }*/


}

