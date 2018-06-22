package com.newlandframework.rpc.boot;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileLock;

/**
 * Created by Administrator on 2018-06-17.
 */
public class Bootinit {
    private static Bootinit ourInstance = new Bootinit();

    public static Bootinit getInstance() {
        return ourInstance;
    }

    private Bootinit() {
        FileOutputStream fos;
        String userDir = System.getProperty("user.dir");
        //打印参数
        //加载Log4j配置
        File logbackFile = new File(userDir+"/logback.xml");
        if (logbackFile.exists()) {
            LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(lc); lc.reset();
            try {
                configurator.doConfigure(logbackFile);
            } catch (JoranException e) {
                e.printStackTrace(System.err);
                System.exit(-1);
            }
        }

        //String logConfPath = System.getProperty("user.dir")+"\\log4j.properties";
        //PropertyConfigurator.configure(logConfPath);
        // 保证程序只运行一个
        FileLock lck;
        try {
            fos = new FileOutputStream(userDir + "/lock");
            lck = fos.getChannel().tryLock();
            if (lck == null) {
                System.out.println("程序已经在运行了，5秒后此程序退出！");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.exit(-1);
                //return;
            }

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
