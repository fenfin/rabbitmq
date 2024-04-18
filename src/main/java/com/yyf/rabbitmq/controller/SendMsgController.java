package com.yyf.rabbitmq.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Author: yyf
 * @Date: 2023/11/24/下午5:37
 * @Description:
 */
@RestController
@RequestMapping("/ttl")
@Slf4j
public class SendMsgController {

    @Resource
    private RabbitTemplate rabbitTemplate;

    //发消息
    @GetMapping("/sendMsg/{message}")
    public void sendMsg(@PathVariable String message){
        System.out.println("当前时间"+new Date().toString()+"发送一条消息给两个ttl队列:"+message);
      rabbitTemplate.convertAndSend("X","XA","消息来自ttl为10s的队列："+message);
      rabbitTemplate.convertAndSend("X","XB","消息来自ttl为40s的队列："+message);
    }

    //开始发消息 消息ttl
    @GetMapping("/sendExpirationMsg/{message}/{ttlTime}")
    public void sendMsg(@PathVariable String message, @PathVariable String ttlTime){
        System.out.println("当前时间"+new Date().toString()+"发送一条消息给两个ttl队列:"+message+"ttl时间："+ttlTime);
        rabbitTemplate.convertAndSend("X","XC",message,message1 -> {
            //发送消息的延迟时长
            message1.getMessageProperties().setExpiration(ttlTime);
            return message1;
        });
    }
}
