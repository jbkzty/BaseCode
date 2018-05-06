package com.netty.chapter02.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @author spuerKun
 * @date 18/2/7.
 */
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    /**
     * 当到服务器的连接已经建立之后将被调用
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 当被通知Channel是活跃的时候，发送一条消息
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks", CharsetUtil.UTF_8));
    }

    /**
     * 当从服务器接收到一条消息时被调用
     * <p>
     * 需要注意的是，由于服务器发送的消息可能会被分块接收，这个方法可能会被读取多次
     * 作为一个面向流的协议，TCP保证了字节数组将会按照服务器发送他们的顺序被接收
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf in) throws Exception {
        System.out.println("Client received " + in.toString(CharsetUtil.UTF_8));
    }

    /**
     * 在处理过程中引发异常的调用
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
