package com.netty.chapter02.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * 引导服务器
 * (1) 绑定到服务器将在其上监听并且接受传入连接请求的端口
 * (2) 配置Channel,以将有关的入站消息通知给EchoServerHandler实例
 * <p>
 * NIO的传输大多是指TCP传输
 * <p>
 * --
 * (1) 创建一个ServerBootstrap的实例以引导和绑定服务器
 * (2) 创建并且分配一个NioEventLoopGroup实例进行事件处理，如接收新连接以及读写数据
 * (3) 指定服务器绑定本地的InetSocketAddress
 * (4) 使用一个EchoServerHandler的实例化每一个新的Channel
 * (5) 调用ServerBootstrap.bind()方法来绑定服务器
 *
 * @author spuerKun
 * @date 18/2/7.
 */
public class EchoServer {

    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage: " + EchoServer.class.getSimpleName() + " <port>");
            return;
        }
        int port = Integer.parseInt(args[0]);
        // 调用服务器的start()方法
        new EchoServer(port).start();
    }

    public void start() throws Exception {

        final EchoServerHandler serverHandler = new EchoServerHandler();

        // 创建EventLoopGroup,用来接收和处理连接
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            // 创建ServerBootstrap
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    // 指定所使用的NIO传输Channel
                    .channel(NioServerSocketChannel.class)
                    // 使用指定的套接字设置套接字地址
                    .localAddress(new InetSocketAddress(port))
                    // 添加一个EchoServerHandler到子Channel的ChannelPipeline
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // EchoServerHandler被标注为@Shareable，所以我们可以总是使用同样的实例
                            ch.pipeline().addLast(serverHandler);
                        }
                    });

            // 异步绑定服务器，调用sync()方法阻塞等待直到完成
            ChannelFuture f = b.bind().sync();
            // 获取Channel的CloseFuture,并且阻塞当前线程直到它完成
            f.channel().closeFuture().sync();
        } finally {
            // 关闭EventLoopGroup,释放所有的资源
            group.shutdownGracefully().sync();
        }
    }
}
