package com.zhangheng.chat;

import com.zhangheng.log.printLog.Log;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

class ChatClientHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        Log.Info(s.trim()+"\n");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause.getMessage().indexOf("远程主机强迫关闭了一个现有的连接")>=0){
            Log.Error("服务器关闭");
        }else {
            Log.Error("服务器错误："+ cause.getMessage());
        }
        ctx.close();
    }
}
