package com.yyf.rabbitmq.three;

import com.rabbitmq.client.Channel;
import com.yyf.rabbitmq.util.RabbitMqUtils;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @Author: yyf
 * @Date: 2023/11/22/下午9:33
 * @Description: 消息手动应答
 */
public class Task2 {

    public static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //开启发布确认
        channel.confirmSelect();
        channel.queueDeclare(TASK_QUEUE_NAME,false,false,false,null);

        Scanner sc = new Scanner(System.in);

        while (sc.hasNext()){
            String message = sc.next();
            channel.basicPublish("",TASK_QUEUE_NAME,null,message.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者消息发送成功："+ message);
        }
    }
}
