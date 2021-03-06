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

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.newlandframework.rpc.serialize.RpcSerializeProtocol;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author tangjie<https://github.com/tang-jie>
 * @filename:MessageRecvChannelInitializer.java
 * @description:MessageRecvChannelInitializer功能模块
 * @blogs http://www.cnblogs.com/jietang/
 * @since 2016/10/7
 */
public class MessageRecvChannelInitializer extends ChannelInitializer<SocketChannel> {

    private RpcSerializeProtocol protocol;
    private RpcRecvSerializeFrame frame = null;

    MessageRecvChannelInitializer buildRpcSerializeProtocol(RpcSerializeProtocol protocol) {
        this.protocol = protocol;
        return this;
    }

    MessageRecvChannelInitializer(Map<String, Object> handlerMap) {
        frame = new RpcRecvSerializeFrame(handlerMap);
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new IdleStateHandler(60, 0, 0, TimeUnit.SECONDS));//心跳
        frame.select(protocol, pipeline);

       /* //心跳
        socketChannel.eventLoop().scheduleAtFixedRate(() -> {
            //System.out.println("2 SECONDS....");
            socketChannel.writeAndFlush(Unpooled.copiedBuffer("##HEART##", Charset.defaultCharset()));
        },1,1, TimeUnit.MINUTES);*/
    }
}
