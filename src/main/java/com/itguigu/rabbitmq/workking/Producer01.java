package com.itguigu.rabbitmq.workking;

import com.itguigu.rabbitmq.util.RabbitmqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * 生产者:完成消息手动应答
 *
 * @author darren
 * @create 2021-11-07 9:42
 */
public class Producer01 {
    private static Logger logger = LoggerFactory.getLogger(Producer01.class);

    public static void main(String[] args) throws IOException, TimeoutException {
        logger.info("Producer send message begin...");
        Channel channel = RabbitmqUtils.channel;

        channel.queueDeclare(RabbitmqUtils.QUEUE_NAME, RabbitmqUtils.DURABLE, false, false, null);

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            String message = scanner.next();

            /**
             * 发送一个消息
             *
             * 1.发送到那个交换机
             * 2.路由的 key 是哪个 本次是队列名称
             * 3.支持消息路由头的其他属性等
             * 4.发送消息的消息体
             * MessageProperties.PERSISTENT_TEXT_PLAIN 消息持久化
             */
            channel.basicPublish("", RabbitmqUtils.QUEUE_NAME,
                    MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            logger.info("Producer send message:{} success...", message);
        }
    }


}
