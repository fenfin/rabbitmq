package com.yyf.rabbitmq.two;

import com.rabbitmq.client.Channel;
import com.yyf.rabbitmq.util.RabbitMqUtils;

import java.util.Scanner;

/**
 * @Author: yyf
 * @Date: 2023/11/22/下午9:08
 * @Description: 生产者
 */
public class Task01 {

    public static final String QUEUE_NAME = "hello";

    //发送大量消息
    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()){
            String message = sc.next();
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            System.out.println("消息发送成功");
        }
    }
}
