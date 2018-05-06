package com.netty.chapter02.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.HashMap;

/**
 * 实现ChannelInboundHandlerAdapter接口用来定义相应入站事件的方法
 * <p>
 * 实现业务逻辑代码
 *
 * @author spuerKun
 * @date 18/2/7.
 */
@ChannelHandler.Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 对于每个传入的消息都要调用
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        // 将消息记录到控制台
        System.out.println("Server received: " + in.toString(CharsetUtil.UTF_8));
        // 将接受到的消息写给发送者，而不冲刷出站消息
        ctx.write(in);
    }

    /**
     * 通知ChannelInboundHandler最后一次对channelRead()的调用是当前批量读取中的最后一条消息
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 将未决消息冲刷到远程节点，并且关闭该Channel
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 在读操作期间，有异常抛出时会调用
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        // 关闭channel
        ctx.close();
    }

    public static void main(String[] args) {
        HashMap<String, String> map = new HashMap<String, String>(10);



        map.put("aa", "n");

    }
}
