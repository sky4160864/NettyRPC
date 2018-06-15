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

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.io.File;
import java.net.URL;

/**
 * @author tangjie<https://github.com/tang-jie>
 * @filename:RpcServerStarter.java
 * @description:RpcServerStarter功能模块
 * @blogs http://www.cnblogs.com/jietang/
 * @since 2016/10/7
 */
public class RpcServerStarterJar {
    public static void main(String[] args) {
        String user_dir = System.getProperty("user.dir");
        String file1 = user_dir+"\\system.properties";
        String file2 = user_dir+"\\spring-druid.xml";
        String file3 = user_dir+"\\rpc-invoke-config-server.xml";
        new FileSystemXmlApplicationContext(file2,file3);//生成exe执行程序后，需要用这个

        //获取jar中xml
        //URL fileURL=RpcServerStarterJar.class.getResource("/rpc-invoke-config-server.xml");//jar中的类获得jar包中资源文件的路径
        //new ClassPathXmlApplicationContext("rpc-invoke-config-server.xml");
    }
}

