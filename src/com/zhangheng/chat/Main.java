package com.zhangheng.chat;

import com.zhangheng.chat.ShopChat.ChatServer;
import com.zhangheng.log.printLog.Log;
import com.zhangheng.util.NetUtil;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try {
            Scanner scanner=new Scanner(System.in);
            int nextInt=8888;
            while (true) {
                System.out.println("请输入运行聊天服务器端口号：");
                nextInt = scanner.nextInt();
                if (!NetUtil.isLocalPortUsing(nextInt)){
                    Log.Info("聊天服务器启动中。。。");
                    break;
                }else {
                    Log.Warn(nextInt+"端口已被占用,请换其他端口");
                }
            }
            ChatServer chatServer = new ChatServer(nextInt);
            chatServer.run();
        }catch (Exception e){
            e.printStackTrace();
            try {
                Thread.sleep(5000);
                System.exit(0);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

    }
}
