package com.itgu.rabbitma.simple;

import com.itgu.rabbitmq.util.RabbitmqUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 简单模式
 *
 * @author darren
 * @create 2021-11-06 17:13
 */
public class Producer {

    public static void main(String[] args) throws IOException, TimeoutException {

        // 获取信道
        Channel channel = RabbitmqUtils.getChannel();

        /**
         * 生成一个队列
         *
         * 1.queue:     队列名称
         * 2.durable:   队列里面的消息是否持久化 默认消息存储在内存中
         * 3.exclusive: 该队列是否只供一个消费者进行消费 是否进行共享 true 可以多个消费者消费
         * 4.autoDelete:是否自动删除 最后一个消费者端开连接以后 该队列是否自动删除 true 自动删除
         * 5.arguments: 其他参数
         */
        channel.queueDeclare(RabbitmqUtils.QUEUE_NAME, true, false, false, null);

        String message = "hello world";

        /**
         * 发送一个消息
         *
         * 1.exchange:      发送到那个交换机
         * 2.routingKey:    路由的 key 是哪个
         * 3.props:         支持消息路由头的其他属性等
         * 4.body:          发送消息的消息体
         */
        channel.basicPublish("", RabbitmqUtils.QUEUE_NAME, null, message.getBytes());

        System.out.println("消息发送中。。。。");


    }
}
