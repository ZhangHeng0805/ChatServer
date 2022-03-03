package com.zhangheng.chat.ShopChat;

import com.google.gson.Gson;
import com.zhangheng.chat.utils.Message;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

class ChatServerHandler extends SimpleChannelInboundHandler<String> {

    private static ChannelGroup channelGroup=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.add(channel);
        Gson gson = new Gson();
        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setFrom("1");
        chatInfo.setFrom_name("服务器");
        chatInfo.setTo("all");
        chatInfo.setTo_name("全部");
        chatInfo.setMsgType(2);
        chatInfo.setChatType(1);
        chatInfo.setTime(sdf.format(new Date()));
        InetSocketAddress ipSocket = (InetSocketAddress)channel.remoteAddress();
//        String clientIp = ipSocket.getAddress().getHostAddress();
        String clientIp = channel.remoteAddress().toString();
        String message="<"+clientIp.replace("/127.0.0.1:","")
                +">上线了\n当前在线人数："+channelGroup.size();
        chatInfo.setMessage(message);
        String json = gson.toJson(chatInfo);
        //将该客户加入聊天信息推送给其他在线的客户端
        //该方法会将channelGroup中的所有channel遍历，并发送消息

        channelGroup.writeAndFlush(json);

        Message.printLog("购物APP聊天服务器--[客户端]<"
                +clientIp.replace("/127.0.0.1:","")
                +">上线了 \t 当前在线人数："+channelGroup.size());

    }



    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        super.channelInactive(ctx);
        Channel channel = ctx.channel();
        Gson gson = new Gson();
        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setFrom("1");
        chatInfo.setFrom_name("服务器");
        chatInfo.setTo("all");
        chatInfo.setTo_name("全部");
        chatInfo.setMsgType(2);
        chatInfo.setChatType(1);
        chatInfo.setTime(sdf.format(new Date()));
        InetSocketAddress ipSocket = (InetSocketAddress)channel.remoteAddress();
//        String clientIp = ipSocket.getAddress().getHostAddress();
        String clientIp = channel.remoteAddress().toString();
        String message="<"+clientIp.replace("/127.0.0.1:","")
                +">下线了\n当前在线人数："+channelGroup.size();
        chatInfo.setMessage(message);
        String json = gson.toJson(chatInfo);

        channelGroup.writeAndFlush(json);

        Message.printLog("购物APP聊天服务器--[客户端]<"
                +clientIp.replace("/127.0.0.1:","")
                +">下线了 \t 当前在线人数："+channelGroup.size());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause.getMessage().indexOf("远程主机强迫关闭了一个现有的连接")>=0){

        }else {
            Message.printLog("购物APP聊天服务器错误："+ cause.getMessage());
        }
        ctx.close();
    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        ChatInfo chat = new Gson().fromJson(s, ChatInfo.class);
        if (chat.getFrom()!=null&&chat.getFrom().length()==11&&chat.getMessage().equals("系统0805:我上线了")){
            Gson gson = new Gson();
            ChatInfo chatInfo = new ChatInfo();
            chatInfo.setFrom("1");
            chatInfo.setFrom_name("服务器");
            chatInfo.setTo("all");
            chatInfo.setTo_name("全部");
            chatInfo.setMsgType(2);
            chatInfo.setChatType(1);
            chatInfo.setTime(sdf.format(new Date()));
            String message="用户《"+chat.getFrom_name()+"》<"+chat.getFrom()+">进入聊天室了";
            chatInfo.setMessage(message);
            String s1 = gson.toJson(chatInfo);
            for (Channel ch : channelGroup) {
                ch.writeAndFlush(s1);
            }
            Message.printLog(message);
        }else if (chat.getFrom()!=null&&chat.getFrom().length()==11&&chat.getMessage().equals("系统0805:我下线了")){
            Gson gson = new Gson();
            ChatInfo chatInfo = new ChatInfo();
            chatInfo.setFrom("1");
            chatInfo.setFrom_name("服务器");
            chatInfo.setTo("all");
            chatInfo.setTo_name("全部");
            chatInfo.setMsgType(2);
            chatInfo.setChatType(1);
            chatInfo.setTime(sdf.format(new Date()));
            String message="用户《"+chat.getFrom_name()+"》<"+chat.getFrom()+">退出聊天室了";
            chatInfo.setMessage(message);
            String s1 = gson.toJson(chatInfo);
            for (Channel ch : channelGroup) {
                ch.writeAndFlush(s1);
            }
            Message.printLog(message);
        }
        else {
            for (Channel ch : channelGroup) {
                ch.writeAndFlush(s);
            }
        }
    }
}
