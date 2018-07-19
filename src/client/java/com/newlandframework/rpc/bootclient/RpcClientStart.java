package com.newlandframework.rpc.bootclient;


import com.newlandframework.rpc.boot.Bootinit;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.TimeUnit;


/**
 * Created by Huang Jianhai on 2018/6/15.
 */
public class RpcClientStart {

    public static ClassPathXmlApplicationContext context = null;
    //public static ClassPathXmlApplicationContext ccontext = new ClassPathXmlApplicationContext("classpath:spring-rpc-invoke-config-client-test.xml");
    public static void main(String[] args) throws InterruptedException {
        String version = "0.3";
        if(!Bootinit.init("version"+version)){
            System.out.println("程序已经在运行了，5秒后此程序退出！");
            TimeUnit.MILLISECONDS.sleep(5000);
            return;
        }

        context = new ClassPathXmlApplicationContext("classpath:spring-rpc-client-config.xml");
        System.out.println("=====VERSION["+version+"]=====");
        //File file = new File(System.getProperty("user.dir"));
        //if(file.exists()){

        //}
        //QuartzUtil.jobStart();
        //TimeUnit.MILLISECONDS.sleep(5000);
        //context.destroy();


    }

    /**
     * VERSION描述
     * 0.2 增加心跳机制
     * 0.3 推断为萧山网络异常断开，客户端channelInactive后自动重连（多次断开多次重连）。修改客户端自动重连，先睡5分钟只重连一次
     */
}
