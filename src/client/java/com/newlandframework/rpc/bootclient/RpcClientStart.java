package com.newlandframework.rpc.bootclient;


import com.newlandframework.rpc.boot.Bootinit;
import com.newlandframework.rpc.bootclient.schedul.QuartzUtil;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * Created by Huang Jianhai on 2018/6/15.
 */
public class RpcClientStart {

    public static final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-rpc-client-config.xml");
    //public static ClassPathXmlApplicationContext ccontext = new ClassPathXmlApplicationContext("classpath:spring-rpc-invoke-config-client-test.xml");
    public static void main(String[] args) {
        Bootinit.getInstance();
        QuartzUtil.jobStart();
    }


}
