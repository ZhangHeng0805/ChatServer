package com.zhangheng.chat;

import com.zhangheng.chat.ShopChat.ChatServer;
import com.zhangheng.chat.utils.Message;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        System.out.println("请输入运行聊天服务器端口号：");
        try {
            int nextInt = scanner.nextInt();
            ChatServer chatServer = new ChatServer(nextInt);
            Message.printLog("聊天服务器启动中。。。");
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
