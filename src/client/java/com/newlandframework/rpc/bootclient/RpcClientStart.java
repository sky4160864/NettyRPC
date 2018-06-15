package com.newlandframework.rpc.bootclient;

import com.newlandframework.rpc.bootclient.schedul.QuartzUtil;
import com.newlandframework.rpc.services.ReqMiddleDatasManage;
import com.newlandframework.rpc.services.pojo.ReqMiddleDatas;
import com.newlandframework.rpc.services.pojo.ResMiddleDatas;
import com.newlandframework.rpc.spring.PropertyPlaceholder;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileLock;

/**
 * Created by Huang Jianhai on 2018/6/15.
 */
public class RpcClientStart {
    private static FileOutputStream fos;
    public static final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-rpc-client-config.xml");
    public static void main(String[] args) {
        init();
        QuartzUtil.jobStart();
    }

    public static void init() {
        //打印参数
        System.out.println("程序运行中...");
        //加载Log4j配置
        //String logConfPath = System.getProperty("user.dir")+"\\log4j.properties";
        //PropertyConfigurator.configure(logConfPath);
        // 保证程序只运行一个
        FileLock lck;
        try {
            fos = new FileOutputStream(System.getProperty("user.dir") + "\\lock");
            lck = fos.getChannel().tryLock();
            if (lck == null) {
                System.out.println("程序已经在运行了，5秒后此程序退出！");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return;
            }
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
