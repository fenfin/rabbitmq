package com.yyf.rabbitmq.eight;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.yyf.rabbitmq.util.RabbitMqUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: yyf
 * @Date: 2023/11/24/上午10:47
 * @Description: 死信队列
 */
public class Consumer02 {

    //普通交换机名称

    //死信交换机名称
    public static final String dead_exchange = "dead_exchange";

    //死信队列名称
    public static final String dead_queue = "dead_queue";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        System.out.println("等待接收消息。。。");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            //沉睡1s
            System.out.println("consumer02接受信息:"+new String(message.getBody(), StandardCharsets.UTF_8));
//            System.out.println("接受队列："+ queueName + "绑定建："+ message.getEnvelope().getRoutingKey());
        };

        CancelCallback cancelCallback = consumerTag -> {
            System.out.println(consumerTag+"消息被中断");
        };

        channel.basicConsume(dead_queue,true,deliverCallback, cancelCallback);
    }
}
