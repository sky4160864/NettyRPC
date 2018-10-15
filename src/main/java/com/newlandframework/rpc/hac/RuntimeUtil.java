package com.newlandframework.rpc.hac;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Huang Jianhai on 2018/7/19.
 */
public class RuntimeUtil {

    public static void kill(String processName){
        killByPid(getPID(processName));
    }

    public static void killByPid(String pid){
        try {
            if(pid==null)return;
            Runtime.getRuntime().exec("taskkill /f /pid "+pid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static void run(String processNameFullPath){
        try {
            Runtime.getRuntime().exec(processNameFullPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getPID(String processName){
        String pid = null;
        //tasklist /nh /FI "IMAGENAME eq hacMidServer.exe"
        String cmd = "tasklist /nh /FI \"IMAGENAME eq "+processName+"\"";
        try {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(cmd);
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while((line=br.readLine()) != null){
                if(line.indexOf(processName) != -1){
                    String[] lineArray = line.split(" ");
                    //int idx = 0;
                    for(String s:lineArray){
                        /*System.out.println(idx+":"+s);
                        idx++;*/
                        if(NumericUtil.isNumber(s)){
                            return s;
                        }
                    }
                    /*pid = lineArray[14].trim(); //本机测试为14
                    return pid;*/
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pid;
    }


    public static String getMemory(String processName){
        String Memory = null;
        String cmd = "tasklist /nh /FI \"IMAGENAME eq "+processName+"\"";
        try {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(cmd);
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while((line=br.readLine()) != null){
                if(line.indexOf(processName) != -1){
                    String[] lineArray = line.split(" ");
                    for(String s:lineArray){
                        if(s.contains(",")&&NumericUtil.isNumber(s.replace(",",""))){
                            return s;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Memory;
    }
}
