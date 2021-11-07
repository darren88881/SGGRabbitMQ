package com.itgu.rabbitmq.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author darren
 * @create 2021-11-06 17:48
 */
public class RabbitMQUtils {
    private static Logger logger = LoggerFactory.getLogger(RabbitMQUtils.class);
    public static String  QUEUE_NAME = "hello";
    public static Connection connection;
    public static Channel channel;

    public static Channel getChannel() throws IOException, TimeoutException {
        logger.info("getChannel begin...");
        // 创建链接工厂
        ConnectionFactory factory = new ConnectionFactory();
        logger.info("getChannel factory:{}", factory.toString());

        factory.setHost("192.168.6.8");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("123");

        // 创建链接
        connection = factory.newConnection();
        logger.info("getChannel connection:{}", connection.toString());

        channel = connection.createChannel();
        logger.info("getChannel channel:{}", channel.toString());
        logger.info("getChannel success!");
        return channel;
    }

    public static void closeChannel() {
        logger.info("closeChannel begin...");
        try {
            connection.close();
        } catch (IOException e) {
            logger.error("closeChannel IOException error!", e);
        }
        logger.info("closeChannel success!");
    }

    /**
     * 输入消息关闭
     *
     * @param message
     */
    public static void inputMessageClose(String message) {
        if ("close".equals(message)) {
            RabbitMQUtils.closeChannel();
        }
    }
}
