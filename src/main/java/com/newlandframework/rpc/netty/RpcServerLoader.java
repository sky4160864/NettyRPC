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
package com.newlandframework.rpc.netty;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import com.newlandframework.rpc.serialize.RpcSerializeProtocol;
import com.newlandframework.rpc.core.RpcSystemConfig;
import com.newlandframework.rpc.parallel.RpcThreadPool;

import com.newlandframework.rpc.spring.PropertyPlaceholder;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author tangjie<https://github.com/tang-jie>
 * @filename:RpcServerLoader.java
 * @description:RpcServerLoader功能模块
 * @blogs http://www.cnblogs.com/jietang/
 * @since 2016/10/7
 */
public class RpcServerLoader {

    private static volatile RpcServerLoader rpcServerLoader;
    private static final String DELIMITER = RpcSystemConfig.DELIMITER;
    private RpcSerializeProtocol serializeProtocol = RpcSerializeProtocol.JDKSERIALIZE;
    private static final int PARALLEL = RpcSystemConfig.SYSTEM_PROPERTY_PARALLEL * 2;
    private EventLoopGroup eventLoopGroup = new NioEventLoopGroup(PARALLEL);
    private static int threadNums = RpcSystemConfig.SYSTEM_PROPERTY_THREADPOOL_THREAD_NUMS;
    private static int queueNums = RpcSystemConfig.SYSTEM_PROPERTY_THREADPOOL_QUEUE_NUMS;
    private static ListeningExecutorService threadPoolExecutor = MoreExecutors.listeningDecorator((ThreadPoolExecutor) RpcThreadPool.getExecutor(threadNums, queueNums));
    private MessageSendHandler messageSendHandler = null;
    private Lock lock = new ReentrantLock();
    private Condition connectStatus = lock.newCondition();
    private Condition handlerStatus = lock.newCondition();

    public static AtomicInteger connectNumbers = new AtomicInteger(0);//连接数

    private RpcServerLoader() {
    }

    public static RpcServerLoader getInstance() {
        if (rpcServerLoader == null) {
            synchronized (RpcServerLoader.class) {
                if (rpcServerLoader == null) {
                    rpcServerLoader = new RpcServerLoader();
                }
            }
        }
        return rpcServerLoader;
    }

    public void load(String serverAddress, RpcSerializeProtocol serializeProtocol) {
        String[] ipAddr = serverAddress.split(RpcServerLoader.DELIMITER);
        if (ipAddr.length == RpcSystemConfig.IPADDR_OPRT_ARRAY_LENGTH) {
            String host = ipAddr[0];
            int port = Integer.parseInt(ipAddr[1]);
            final InetSocketAddress remoteAddr = new InetSocketAddress(host, port);

            System.out.printf("[author tangjie] Netty RPC Client start success!\nip:%s\nport:%d\nprotocol:%s\n\n", host, port, serializeProtocol);

            ListenableFuture<Boolean> listenableFuture = threadPoolExecutor.submit(new MessageSendInitializeTask(eventLoopGroup, remoteAddr, serializeProtocol));

            Futures.addCallback(listenableFuture, new FutureCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean result) {
                    try {
                        lock.lock();

                        if (messageSendHandler == null) {
                            handlerStatus.await();
                        }

                        if (result.equals(Boolean.TRUE) && messageSendHandler != null) {
                            connectStatus.signalAll();
                        }
                    } catch (InterruptedException ex) {
                        Logger.getLogger(RpcServerLoader.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        lock.unlock();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    t.printStackTrace();
                }
            }, threadPoolExecutor);
        }
    }

    //设置MessageSendHandler后，唤醒handlerStatus
    public void setMessageSendHandler(MessageSendHandler messageInHandler) {
        try {
            lock.lock();
            this.messageSendHandler = messageInHandler;
            handlerStatus.signal();
        } finally {
            lock.unlock();
        }
    }

    //设置MessageSendHandler为空 hjh20180621
    public void setMessageSendHandler() {
        try {
            lock.lock();
            if(this.messageSendHandler!=null){
                Channel channel = this.messageSendHandler.getChannel();
                if(channel!=null){
                    //if(channel!=null&&channel.isOpen()){
                    channel.close();
                }
            }
        } finally {
            this.messageSendHandler = null;
            lock.unlock();
        }
    }

    public MessageSendHandler getMessageSendHandler() throws InterruptedException {
        try {
            lock.lock();
            if (messageSendHandler == null) {
                connectStatus.await();
            }
            return messageSendHandler;
        } finally {
            lock.unlock();
        }
    }

    //根据messageSendHandler判断是否可以发送消息（MessageSendHandler.channelInactive触发后messageSendHandler设置null） hjh20180621
    public boolean getMessageSendStatus(){
        try {
            lock.lock();
            if (messageSendHandler == null) {
                return false;
            }
            return true;
        } finally {
            lock.unlock();
        }
    }

    public void reLoad(){
        try {
            lock.lock();
            if(messageSendHandler != null){
                setMessageSendHandler();
                TimeUnit.MILLISECONDS.sleep(5*60*1000);
                String ipAddr = PropertyPlaceholder.getProperty("rpc.server.addr");
                String protocol = PropertyPlaceholder.getProperty("rpc.server.protocol","PROTOSTUFFSERIALIZE");
                if(ipAddr!=null&&protocol!=null){
                    load(ipAddr,RpcSerializeProtocol.valueOf(protocol));
                }
            }
        } catch (InterruptedException e) {
        } finally {
            lock.unlock();
        }
    }

    public void unLoad() {
        if(messageSendHandler!=null){
            messageSendHandler.close();
        }

        threadPoolExecutor.shutdown();
        eventLoopGroup.shutdownGracefully();
    }

    public void setSerializeProtocol(RpcSerializeProtocol serializeProtocol) {
        this.serializeProtocol = serializeProtocol;
    }
}
