package com.newlandframework.rpc.hac;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Huang Jianhai on 2018/7/20.
 */
public class VersionCtl {
    private static Logger logger = LoggerFactory.getLogger(VersionCtl.class);
    public static Map<String, String> versionMap = new ConcurrentHashMap<String, String>();
    public static Logger getLog(){
        return logger;
    }
}
