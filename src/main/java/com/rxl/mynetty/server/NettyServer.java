package com.rxl.mynetty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.sctp.nio.NioSctpServerChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * ClassName: NettyServer
 * Description: Netty服务端
 *
 * @author ranxinlong@cirdb.cn
 * @version 1.0.0
 * @date 2021/01/14
 */
@Slf4j
public class NettyServer {

    /**
     * 端口
     */
    private final int port;

    public NettyServer(int port) {
        this.port = port;
    }

    /**
     * soket服务监听
     */
    public void start() throws Exception{
        //可以理解为是一个线程池
        EventLoopGroup eventExecutors = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(eventExecutors)
                    // 指定使用的channel 通道
                    .channel(NioServerSocketChannel.class).localAddress(port)
                    // 绑定客户端连接时候触发操作
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            //当客户端连接以后，会在这里显示一下的
                            log.info("客户端:{},连接成功",channel.localAddress().getHostName());
                            // 客户端触发操作
                            channel.pipeline().addLast(new MyServerHandler());
                        }
                    });

            // 服务器异步创建绑定
            ChannelFuture f = serverBootstrap.bind(port).sync();
            // 关闭服务器通道
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("netty服务启动失败");
        } finally {
            //关闭服务
            eventExecutors.shutdownGracefully().sync();
        }

    }

    public static void main(String[] args) {
        try {
            new NettyServer(65535).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
