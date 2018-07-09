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
package com.newlandframework.test;

import com.newlandframework.rpc.services.AddCalculate;
import com.newlandframework.rpc.services.MultiCalculate;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author tangjie<https://github.com/tang-jie>
 * @filename:RpcParallelTest.java
 * @description:RpcParallelTest功能模块
 * @blogs http://www.cnblogs.com/jietang/
 * @since 2016/10/7
 */
public class RpcParallelTest2 {

    public static void parallelAddCalcTask(AddCalculate calc, int parallel,CountDownLatch signal,CountDownLatch finish) throws InterruptedException {
        for (int index = 0; index < parallel; index++) {
            AddCalcParallelRequestThread client = new AddCalcParallelRequestThread(calc, signal, finish, index);
            new Thread(client).start();
        }
    }

    public static void parallelMultiCalcTask(MultiCalculate calc, int parallel,CountDownLatch signal,CountDownLatch finish) throws InterruptedException {
        for (int index = 0; index < parallel; index++) {
            MultiCalcParallelRequestThread client = new MultiCalcParallelRequestThread(calc, signal, finish, index);
            new Thread(client).start();
        }
    }

    public static void addTask(AddCalculate calc, int parallel,CountDownLatch signal,CountDownLatch finish) throws InterruptedException {
        RpcParallelTest2.parallelAddCalcTask(calc, parallel,signal,finish);
        TimeUnit.MILLISECONDS.sleep(30);
    }

    public static void multiTask(MultiCalculate calc, int parallel,CountDownLatch signal,CountDownLatch finish) throws InterruptedException {
        RpcParallelTest2.parallelMultiCalcTask(calc, parallel,signal,finish);
        TimeUnit.MILLISECONDS.sleep(30);
    }

    public static void main(String[] args) throws Exception {
        //开始计时
        StopWatch sw = new StopWatch();
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:rpc-invoke-config-client.xml");
        for (int k = 0; k < 100; k++) {
            //并行度1000
            int parallel = 1000+100*k;
            CountDownLatch signal = new CountDownLatch(1);
            CountDownLatch finish = new CountDownLatch(parallel*2);
            for (int i = 0; i < 1; i++) {
                addTask((AddCalculate) context.getBean("addCalc"), parallel,signal,finish);
                multiTask((MultiCalculate) context.getBean("multiCalc"), parallel,signal,finish);
                signal.countDown();
                sw.start();
                finish.await();
                sw.stop();
                String tip = String.format("计算RPC调用并行度[%d]总共耗时: [%s] 毫秒",parallel, sw.getTime());
                System.out.println(tip);
                System.out.printf("[author tangjie] Netty RPC Server 消息协议序列化第[%d]轮并发验证结束!\n\n", i);
                sw.reset();
            }
        }

        context.destroy();
    }
}
