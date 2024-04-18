package com.yyf.rabbitmq.three;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.yyf.rabbitmq.util.RabbitMqUtils;
import com.yyf.rabbitmq.util.SleepUtils;

import java.nio.charset.StandardCharsets;

/**
 * @Author: yyf
 * @Date: 2023/11/22/下午9:37
 * @Description:
 */
public class Work04 {

    public static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            //沉睡30s
            SleepUtils.sleep(30);
            System.out.println("接受信息:"+new String(message.getBody(), StandardCharsets.UTF_8));
            //手动应答
            /**
             * 1、消息标记tag
             * 2、是否批量应答
             * **/
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
        };

        CancelCallback cancelCallback = consumerTag -> {
            System.out.println(consumerTag+"消息被中断");
        };
        int prefetch = 1;
//        channel.p
        System.out.println("C2等待接受消息处理时间较长.....");
        //采用手动应答
        boolean autoAck = false;
        channel.basicConsume(TASK_QUEUE_NAME,autoAck,deliverCallback,cancelCallback);
    }
}
