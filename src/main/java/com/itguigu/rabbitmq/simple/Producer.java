package com.itguigu.rabbitmq.simple;

import com.itguigu.rabbitmq.util.RabbitmqUtils;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 简单模式
 * simple:一人发一人收
 *
 * 生产者
 * @author darren
 * @create 2021-11-06 17:13
 */
public class Producer {

    private static Logger logger = LoggerFactory.getLogger(Producer.class);

    public static void main(String[] args) throws IOException, TimeoutException {

        // 创建简单模式的队列
        Channel channel = RabbitmqUtils.createSimpleQueue();

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

        logger.info("消息发送中。。。。");
    }
}
