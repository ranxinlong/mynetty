package com.rxl.mynetty.server;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * ClassName: MyServerHandler
 * Description: MyServerHandler service impl
 *
 * @author ranxinlong@cirdb.cn
 * @version 1.0.0
 * @date 2021/01/14
 */
@Slf4j
public class MyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 功能：读取服务器发送过来的信息 必须重写
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("服务端收到的消息是:{}",msg);
        ctx.write(msg);
    }

    /**
     * 数据读取完成以后的操作
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
       //ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener)
        ctx.flush();
    }

    /**
     * 捕获服务器的异常
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
