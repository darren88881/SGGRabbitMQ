package com.itgu.rabbitma.workking;

import com.itgu.rabbitmq.util.RabbitmqUtils;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @author darren
 * @create 2021-11-07 9:42
 */
public class Producer01 {
    private static Logger logger = LoggerFactory.getLogger(Producer01.class);

    public static void main(String[] args) throws IOException, TimeoutException {
        logger.info("Producer send message begin...");
        Channel channel = RabbitmqUtils.getChannel();

        channel.queueDeclare(RabbitmqUtils.QUEUE_NAME, false, false, false, null);

        Scanner scanner = new Scanner(System.in);

        logger.info("请输入发送的消息，输入close 结束发送：");
        while (scanner.hasNext()) {
            String message = scanner.next();

            /**
             * 发送一个消息
             * 1.发送到那个交换机
             * 2.路由的 key 是哪个
             * 3.支持消息路由头的其他属性等
             * 4.发送消息的消息体
             */
            channel.basicPublish("", RabbitmqUtils.QUEUE_NAME, null, message.getBytes());
            logger.info("Producer send message:{} success...", message);
        }
    }


}
