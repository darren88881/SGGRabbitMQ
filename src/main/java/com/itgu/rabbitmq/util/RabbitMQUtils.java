package com.itgu.rabbitmq.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author darren
 * @create 2021-11-06 17:48
 */
public class RabbitMQUtils {
    public static String  QUEUE_NAME = "hello";

    public static Connection connection;
    public static Channel channel;

    public static Channel getChannel() throws IOException, TimeoutException {
        // 创建链接工厂
        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost("192.168.6.8");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("123");

        // 创建链接
        connection = factory.newConnection();

        channel = connection.createChannel();

        return channel;
    }

    public static void closeChannel() throws IOException, TimeoutException {
        channel.close();
        connection.close();
    }

}
