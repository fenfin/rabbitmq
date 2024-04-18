package com.yyf.rabbitmq.seven;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.yyf.rabbitmq.util.RabbitMqUtils;
import com.yyf.rabbitmq.util.SleepUtils;

import java.nio.charset.StandardCharsets;

/**
 * @Author: yyf
 * @Date: 2023/11/24/上午10:08
 * @Description:
 * 声明主题交换机
 * 消费者c1
 */
public class ReceiveLogsTopic01 {

    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME,"topic");

        //声明队列
        String queueName = "Q1";
        channel.queueDeclare(queueName,false,false,false,null);
        channel.queueBind(queueName,EXCHANGE_NAME,"*.orange.*");
        System.out.println("等待接收消息....");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            //沉睡1s
            System.out.println("接受信息:"+new String(message.getBody(), StandardCharsets.UTF_8));
            System.out.println("接受队列："+ queueName + "绑定建："+ message.getEnvelope().getRoutingKey());
        };

        CancelCallback cancelCallback = consumerTag -> {
            System.out.println(consumerTag+"消息被中断");
        };

        channel.basicConsume(queueName,true,deliverCallback, cancelCallback);
    }
}
