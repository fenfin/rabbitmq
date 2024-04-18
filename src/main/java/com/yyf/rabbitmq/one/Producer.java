package com.yyf.rabbitmq.one;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

//生产者
public class Producer {

    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.1.6");
        factory.setUsername("guest");
        factory.setPassword("guest");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //生成队列
        /**
         * 1、队列名称
         * 2、队列里面消息是否持久化
         * 3、该队列是否只供一个消费者进行消费 是否进行消息共享
         * 4、是否自动删除 最后一个消费者断开连接
         * 5、其它参数
         * */
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        String message = "hello world";
        /**
         * 1、使用哪个交换机
         * 2、路由key值，本次是队列名称
         * 3、其它参数
         * 4、消息体
         * */
        channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
        System.out.println("消息发送成功");
    }
}
