package com.newlandframework.rpc.boot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 判断网络连接状况.
 *
 */
public class NetState {
    private static Logger logger = LoggerFactory.getLogger(NetState.class);
    public static boolean isConnect(String ip){
        boolean connect = false;
        Runtime runtime = Runtime.getRuntime();
        Process process;
        try {
            process = runtime.exec("ping " + ip);
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            //System.out.println("返回值为:"+sb);
            is.close();
            isr.close();
            br.close();

            if (null != sb && !sb.toString().equals("")) {
                //String logString = "";
                if (sb.toString().indexOf("TTL") > 0) {
                    // 网络畅通
                    connect = true;
                    logger.error("[PING {}]",ip);
                } else {
                    // 网络不畅通
                    connect = false;
                    logger.error("[PING {} ERR]",ip);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connect;
    }

    /*public static void main(String[] args) {
        NetState netState = new NetState();
        System.out.println(netState.isConnect());

    }*/
}
