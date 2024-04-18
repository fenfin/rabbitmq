package com.yyf.rabbitmq.seven;

import com.rabbitmq.client.Channel;
import com.yyf.rabbitmq.util.RabbitMqUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: yyf
 * @Date: 2023/11/24/上午10:18
 * @Description:
 * 神=生产者
 */
public class EmitLogTopic {

    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

//        channel.exchangeDeclare(EXCHANGE_NAME,"topic");
        Map<String,String> binfKeyMap = new HashMap<>();
        binfKeyMap.put("quick.orange.rabbit","Q1Q2");
        binfKeyMap.put("lazy.orange.elephant","Q1Q2");
        binfKeyMap.put("quick.orange.fox","Q1");
        binfKeyMap.put("lazy.brown.fox","Q2");
        binfKeyMap.put("lazy.pink.rabbit","Q2");
        binfKeyMap.put("quick.brown.fox","NULL");
        //声明队列
        for (Map.Entry<String, String> entry : binfKeyMap.entrySet()) {
            String key = entry.getKey();
            String message = entry.getValue();
            channel.basicPublish(EXCHANGE_NAME,key,null,message.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发送消息"+message);
        }
    }
}
