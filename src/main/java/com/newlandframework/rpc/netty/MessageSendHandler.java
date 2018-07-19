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

import com.newlandframework.rpc.core.RpcSystemConfig;
import com.newlandframework.rpc.serialize.RpcSerializeProtocol;
import com.newlandframework.rpc.spring.PropertyPlaceholder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;

import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.newlandframework.rpc.core.MessageCallBack;
import com.newlandframework.rpc.model.MessageRequest;
import com.newlandframework.rpc.model.MessageResponse;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author tangjie<https://github.com/tang-jie>
 * @filename:MessageSendHandler.java
 * @description:MessageSendHandler功能模块
 * @blogs http://www.cnblogs.com/jietang/
 * @since 2016/10/7
 */
public class MessageSendHandler extends ChannelInboundHandlerAdapter {
    private Logger logger = LoggerFactory.getLogger(MessageSendHandler.class);
    //private static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Heartbeat",
    //        CharsetUtil.UTF_8));
    private MessageRequest heartBeat = new MessageRequest();

    //private static final int TRY_TIMES = 3;
    //private int currentTime = 0;

    private ConcurrentHashMap<String, MessageCallBack> mapCallBack = new ConcurrentHashMap<String, MessageCallBack>();
    private volatile Channel channel;
    private SocketAddress remoteAddr;

    public Channel getChannel() {
        return channel;
    }

    public SocketAddress getRemoteAddr() {
        return remoteAddr;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.remoteAddr = this.channel.remoteAddress();
        heartBeat.setClassName(RpcSystemConfig.HEART_BEAT);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        this.channel = ctx.channel();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //System.out.println("循环触发时间："+ new Date());
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.WRITER_IDLE) {
                ctx.channel().writeAndFlush(heartBeat).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MessageResponse response = (MessageResponse) msg;
        String messageId = response.getMessageId();
        MessageCallBack callBack = mapCallBack.get(messageId);
        if (callBack != null) {
            mapCallBack.remove(messageId);
            callBack.over(response);
        }
    }
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("channelInactive...");
        RpcServerLoader.getInstance().reLoad();
       /* RpcServerLoader.getInstance().setMessageSendHandler();
        String ipAddr = PropertyPlaceholder.getProperty("rpc.server.addr");
        String protocol = PropertyPlaceholder.getProperty("rpc.server.protocol","PROTOSTUFFSERIALIZE");
        if(ipAddr!=null&&protocol!=null){
            RpcServerLoader.getInstance().load(ipAddr,RpcSerializeProtocol.valueOf(protocol));
        }*/
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        /*if(cause.getMessage().contains("远程主机强迫关闭了一个现有的连接")){
            logger.info("远程主机强迫关闭了一个现有的连接...");
            RpcServerLoader.getInstance().setMessageSendHandler();
            String ipAddr = PropertyPlaceholder.getProperty("rpc.server.addr");
            String protocol = PropertyPlaceholder.getProperty("rpc.server.protocol","PROTOSTUFFSERIALIZE");
            if(ipAddr!=null&&protocol!=null){
                RpcServerLoader.getInstance().load(ipAddr,RpcSerializeProtocol.valueOf(protocol));
            }
        }else{
            logger.info(cause.getMessage());
        }*/
        logger.info(cause.getMessage());
        //cause.printStackTrace();
        ctx.close();
    }

    public void close() {
        channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    public MessageCallBack sendRequest(MessageRequest request) {
        MessageCallBack callBack = new MessageCallBack(request);
        mapCallBack.put(request.getMessageId(), callBack);
        channel.writeAndFlush(request);
        return callBack;
    }
}
