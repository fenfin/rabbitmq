package com.yyf.rabbitmq.one;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {

    public static final String QUEUE_NAME =  "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.1.6");
        factory.setUsername("guest");
        factory.setPassword("guest");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        DeliverCallback deliverCallback = (consumerTag,message) -> {
            System.out.println("接受信息");
            System.out.println(new String(message.getBody()));
        };

        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("消息被中断");
        };

        /**
         * 消费者消费消息
         * 1、消费的队列
         * 2、消费成功是否自动回应
         * 3、消费者消费未成功回调
         * 4、取消消息回调
         * */
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}
