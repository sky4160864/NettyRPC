package com.newlandframework.rpc.boot;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.channels.FileLock;

/**
 * Created by Administrator on 2018-06-17.
 */
public class Bootinit {
    private static Bootinit ourInstance = new Bootinit();
    public boolean flag = false;

    public static Bootinit getInstance() {
        return ourInstance;
    }



    private Bootinit() {
        String userDir = System.getProperty("user.dir");
        // 保证程序只运行一个
        try {
            File file = new File(userDir + "/lock");
            if(!file.exists()){
                file.createNewFile();
            }
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            FileLock fileLock = randomAccessFile.getChannel().tryLock();
            if (fileLock!=null&&fileLock.isValid()) {
                flag = true;
            }

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }


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
    }
}
