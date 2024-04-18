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
public class Consumer01 {

    //普通交换机名称
    public static final String normal_exchange = "normal_exchange";

    //死信交换机名称
    public static final String dead_exchange = "dead_exchange";

    //普通队列名称
    public static final String normal_queue = "normal_queue";

    //死信队列名称
    public static final String dead_queue = "dead_queue";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();

        //声明交换机
        channel.exchangeDeclare(normal_exchange, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(dead_exchange,BuiltinExchangeType.DIRECT);

        //声明普通队列
        Map<String,Object> arguments = new HashMap<>();
        //过期时间
        //arguments.put("x-message-ttl",100000);
        //正常队列设置死信交换机
        arguments.put("x-dead-letter-exchange",dead_exchange);
        //正常队列设置死信RoutingKey
        arguments.put("x-dead-letter-routing-key","lisi");
        //设置正常队列长度限制
//        arguments.put("x-max-length",6);
        channel.queueDeclare(normal_queue,false,false,false,arguments);

        //声明死信队列
        channel.queueDeclare(dead_queue,false,false,false,null);

        //绑定普通交换机与普通队列
        channel.queueBind(normal_queue,normal_exchange,"zhangsan");
        //绑定死信交换机与死信队列
        channel.queueBind(dead_queue,dead_exchange,"lisi");
        System.out.println("等待接收消息....");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            //沉睡1s
            String msg= new String(message.getBody(), StandardCharsets.UTF_8);
            if (msg.equals("info5")){
                System.out.println("consumer01拒接的消息"+ msg);
                channel.basicReject(message.getEnvelope().getDeliveryTag(),false);
            }else{
                System.out.println("consumer01接受信息:"+msg);
                channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
            }
//            System.out.println("接受队列："+ queueName + "绑定建："+ message.getEnvelope().getRoutingKey());
        };

        CancelCallback cancelCallback = consumerTag -> {
            System.out.println(consumerTag+"消息被中断");
        };

        channel.basicConsume(normal_queue,false,deliverCallback, cancelCallback);
    }
}
