package com.yyf.rabbitmq.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author: yyf
 * @Date: 2023/11/24/下午5:43
 * @Description:
 */
@Slf4j
@Component
public class DeadLetterQueueConsumer {

    //接受消息
    @RabbitListener(queues = "QD")
    public void receiveD(Message message, Channel channel) throws Exception{
        String msg = new String(message.getBody());
        System.out.println("当前时间"+new Date().toString()+"收到消息:"+msg);
//        log.info("当前时间：{}，收到消息:",new Date().toString(), msg);
    }
}
