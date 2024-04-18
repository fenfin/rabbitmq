package com.yyf.rabbitmq.eight;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.yyf.rabbitmq.util.RabbitMqUtils;

import java.nio.charset.StandardCharsets;

/**
 * @Author: yyf
 * @Date: 2023/11/24/上午11:08
 * @Description: 死信队列 生产者
 */
public class Producer {

    public static final String normal_exchange = "normal_exchange";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();

        //死信消息 设置ttl时间 ms
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().expiration("10000").build();

        for (int i = 1; i < 11; i++) {
            String  message = "info"+i;
            channel.basicPublish(normal_exchange,"zhangsan",properties, message.getBytes(StandardCharsets.UTF_8));
        }
    }
}
