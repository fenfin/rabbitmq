package com.yyf.rabbitmq.four;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.yyf.rabbitmq.util.RabbitMqUtils;

import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @Author: yyf
 * @Date: 2023/11/23/下午3:52
 * @Description:
 */
public class ConfirmMessage {

    //批量发消息个数
    public static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception{
        //ConfirmMessage.publishMessageOne();
        //ConfirmMessage.publishMessageBatch();
        ConfirmMessage.publishMessageAsync();
    }

    //单个确认
    public static void publishMessageOne() throws Exception{
        Channel channel = RabbitMqUtils.getChannel();

        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,true,false,false,null);
        channel.confirmSelect();

        long begin = System.currentTimeMillis();

        //批量发消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i+"";
            channel.basicPublish("",queueName,null,message.getBytes());

            //单个消息马上进行发布确认
            boolean flag = channel.waitForConfirms();
            if (flag){
                System.out.println("消息发送成功");
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("发布"+MESSAGE_COUNT+"个单独确认消息，耗时："+(end-begin));
    }

    //批量确认发布
    public static void publishMessageBatch() throws Exception{
        Channel channel1 = RabbitMqUtils.getChannel();

        String queueName = UUID.randomUUID().toString();
        channel1.queueDeclare(queueName,true,false,false,null);
        channel1.confirmSelect();

        long begin = System.currentTimeMillis();

        //批量长度
        int batchSize = 100;

        //批量发消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i+"";
            channel1.basicPublish("",queueName,null,message.getBytes());
            if(i%batchSize == 0){
                channel1.waitForConfirms();
            }
//            //单个消息马上进行发布确认
//            boolean flag = channel1.waitForConfirms();
//            if (flag){
//                System.out.println("消息发送成功");
//            }
        }
        long end = System.currentTimeMillis();
        System.out.println("发布"+MESSAGE_COUNT+"个批量确认消息，耗时："+(end-begin));
    }

    //异步确认
    public static void publishMessageAsync() throws Exception{
        Channel channel = RabbitMqUtils.getChannel();

        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,true,false,false,null);
        channel.confirmSelect();

        long begin = System.currentTimeMillis();

        /**
         * 线程安全有序的一个哈希表，适用于高并发情况
         * 1、轻松将消息和序号进行关联
         * 2、轻松批量删除条目 只要给到序号
         * 3、支持高并发
         *
         * */
        ConcurrentSkipListMap<Long,String> outStandingConfirms = new ConcurrentSkipListMap<>();

        //消息确认成功 回调函数
        ConfirmCallback ackConfirm = (deliveryTag,multiple) -> {
            if (multiple){
                //如果批量
                //2、删除已经确认的消息
                ConcurrentNavigableMap<Long, String> confirmed = outStandingConfirms.headMap(deliveryTag);
            }else {
                outStandingConfirms.remove(deliveryTag);
            }
            System.out.println("确认的消息："+deliveryTag);
        };

        //消息确认失败 回调函数
        /**
         * 1、消息的标记
         * 2、是否为批量确认
         * */
        ConfirmCallback nackConfirm = (deliveryTag,multiple) -> {
            String message = outStandingConfirms.get(deliveryTag);
            System.out.println("未确认的消息："+message+"标记："+deliveryTag);
        };

        //监听消息接口 监听那些成功，那些失败
        /**
         * 1、监听哪些消息成功
         * 2、监听那些消息失败
         * */
        channel.addConfirmListener(ackConfirm,nackConfirm);

        //批量发消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = "消息"+i;
            channel.basicPublish("",queueName,null,message.getBytes());
            //1、此处记录下所有发送的消息 消息的总和
            outStandingConfirms.put(channel.getNextPublishSeqNo(),message);
        }
        long end = System.currentTimeMillis();
        System.out.println("发布"+MESSAGE_COUNT+"个异步确认消息，耗时："+(end-begin));
    }

}
