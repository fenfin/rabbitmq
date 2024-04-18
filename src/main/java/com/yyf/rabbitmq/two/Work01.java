package com.yyf.rabbitmq.two;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.yyf.rabbitmq.util.RabbitMqUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author: yyf
 * @Date: 2023/11/22/下午8:56
 * @Description:
 */
public class Work01 {

    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("接受信息:"+new String(message.getBody()));
        };

        CancelCallback cancelCallback = consumerTag -> {
            System.out.println(consumerTag+"消息被中断");
        };
        System.out.println("C2等待接受消息.....");
        Channel channel = RabbitMqUtils.getChannel();
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}
