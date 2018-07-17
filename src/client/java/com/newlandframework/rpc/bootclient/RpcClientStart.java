package com.newlandframework.rpc.bootclient;


import com.newlandframework.rpc.boot.Bootinit;
//import com.newlandframework.rpc.bootclient.schedul.QuartzUtil;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.TimeUnit;


/**
 * Created by Huang Jianhai on 2018/6/15.
 */
public class RpcClientStart {

    public static ClassPathXmlApplicationContext context = null;
    //public static ClassPathXmlApplicationContext ccontext = new ClassPathXmlApplicationContext("classpath:spring-rpc-invoke-config-client-test.xml");
    public static void main(String[] args) throws InterruptedException {
        if(!Bootinit.getInstance().flag){
            System.out.println("程序已经在运行了，5秒后此程序退出！");
            TimeUnit.MILLISECONDS.sleep(5000);
            return;
        }

        context = new ClassPathXmlApplicationContext("classpath:spring-rpc-client-config.xml");
        System.out.println("=====VERSION[0.2]=====");
        //QuartzUtil.jobStart();
        //TimeUnit.MILLISECONDS.sleep(5000);
        //context.destroy();


    }

    /**
     * VERSION描述
     * 0.2 增加心跳机制
     */
}
