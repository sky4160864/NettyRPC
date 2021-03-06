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

import com.newlandframework.rpc.bootclient.RpcClientStart;
import com.newlandframework.rpc.core.RpcSystemConfig;
import com.newlandframework.rpc.hac.VersionCtl;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.Callable;

import com.newlandframework.rpc.model.MessageRequest;
import com.newlandframework.rpc.model.MessageResponse;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author tangjie<https://github.com/tang-jie>
 * @filename:MessageRecvHandler.java
 * @description:MessageRecvHandler功能模块
 * @blogs http://www.cnblogs.com/jietang/
 * @since 2016/10/7
 */
public class MessageRecvHandler extends ChannelInboundHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(MessageRecvHandler.class);
    private final Map<String, Object> handlerMap;
    private int loss_connect_time = 0;

    public MessageRecvHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("{} connect:{}",ctx.channel().remoteAddress().toString(),RpcServerLoader.connectNumbers.incrementAndGet());
        ctx.fireChannelActive();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //System.out.println("----userEventTriggered----"+new Date());
        if (evt instanceof IdleStateEvent) {
            //System.out.println(loss_connect_time+"----Heart----"+new Date());
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                loss_connect_time++;
                if (loss_connect_time > 2) {
                    logger.error("[No Heart Close]{}",ctx.channel().remoteAddress().toString());
                    ctx.channel().close();
                    ctx.close();
                }
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            MessageRequest request = (MessageRequest) msg;
            loss_connect_time = 0;
            //心跳
            if(RpcSystemConfig.HEART_BEAT.equals(request.getClassName())){
                return;
            }
            if(request.getClassName().startsWith("version")){
                if(!RpcClientStart.RPC_VERSION.equals(request.getClassName())){
                    String remoteAddress = ctx.channel().remoteAddress().toString().split(":")[0];
                    if(!VersionCtl.versionMap.containsKey(remoteAddress)){
                        VersionCtl.getLog().info("[VERSION ERR] {}:{}",request.getClassName(),remoteAddress);
                    }
                }
                return;
            }

            MessageResponse response = new MessageResponse();
            if(request.getMessageId()==null){
                logger.error("[MessageId NULL]{}  CLS:{}",ctx.channel().remoteAddress().toString(),request.getClassName());
                return;
            }
            RecvInitializeTaskFacade facade = new RecvInitializeTaskFacade(request, response, handlerMap);
            Callable<Boolean> recvTask = facade.getTask();
            MessageRecvExecutor.submit(recvTask, ctx, request, response);
        }finally {
            ReferenceCountUtil.release(msg);//手动释放
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("{} disconnect:{}",ctx.channel().remoteAddress().toString(),RpcServerLoader.connectNumbers.decrementAndGet());
        super.channelInactive(ctx);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if(cause.getMessage().contains("远程主机强迫关闭了一个现有的连接")){
            System.out.println(ctx.channel().remoteAddress().toString()+"远程主机强迫关闭了一个现有的连接");
        }else{
            cause.printStackTrace();
        }
        ctx.close();
    }
}

