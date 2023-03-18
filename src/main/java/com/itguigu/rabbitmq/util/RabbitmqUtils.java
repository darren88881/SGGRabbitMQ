package com.itguigu.rabbitmq.util;

import com.rabbitmq.client.BuiltinExchangeType;
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
public class RabbitmqUtils {
    private static Logger   logger          = LoggerFactory.getLogger(RabbitmqUtils.class);
    public  static String   QUEUE_NAME      = "hello";

    public  static String   EXCHANGE_NAME                 = "fanout_exchange";
    public  static String   EXCHANGE_QUEUE_NAME1          = "fanout_exchange_queue1";
    public  static String   EXCHANGE_QUEUE_NAME2          = "fanout_exchange_queue2";

    public  static String   DIRECT_EXCHANGE_NAME          = "direct_exchange";
    public  static String   DIRECT_EXCHANGE_ROUTING_KEY1  = "direct_routing_key_1";
    public  static String   DIRECT_EXCHANGE_ROUTING_KEY2  = "direct_routing_key_2";
    public  static String   DIRECT_EXCHANGE_QUEUE_NAME1   = "direct_exchange_queue1";
    public  static String   DIRECT_EXCHANGE_QUEUE_NAME2   = "direct_exchange_queue2";


    public  static Integer  MESSAGE_NUM     = 10;
    /**
     * 队列持久化
     */
    public static boolean   DURABLE = true;
    public static Connection connection;
    public static Channel   channel;

    static {
        logger.info("getChannel begin...");
        // 创建链接工厂
        ConnectionFactory factory = new ConnectionFactory();
        logger.info("getChannel factory:{}", factory.toString());

        factory.setHost("192.168.6.8");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("123");

        // 创建链接
        try {
            connection = factory.newConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
        logger.info("getChannel connection:{}", connection.toString());

        try {
            channel = connection.createChannel();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        logger.info("getChannel channel:{}", channel.toString());
        logger.info("getChannel success!");
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
     * 创建扇出交换机
     * @return
     * @throws IOException
     * @throws TimeoutException
     */
    public static Channel createFanOutExchangeChannel() throws IOException {

        // 删除名为 fanout_exchange 的交换机
        channel.exchangeDelete(RabbitmqUtils.EXCHANGE_NAME);
        /**
         * exchange: 交换机名称,
         * BuiltinExchangeType：交换机类型,
         * durable: 持久化交换机
         */
        channel.exchangeDeclare(RabbitmqUtils.EXCHANGE_NAME, BuiltinExchangeType.FANOUT, true);
        logger.info("创建扇出交换机成功");
        return channel;
    }

    /**
     * 创建直接交换机
     * @return
     * @throws IOException
     * @throws TimeoutException
     */
    public static Channel createDirectExchangeChannel() throws IOException {

        // 删除名为 fanout_exchange 的交换机
        channel.exchangeDelete(RabbitmqUtils.DIRECT_EXCHANGE_NAME);
        /**
         * exchange: 交换机名称,
         * BuiltinExchangeType：交换机类型,
         * durable: 持久化交换机
         */
        channel.exchangeDeclare(RabbitmqUtils.DIRECT_EXCHANGE_NAME, BuiltinExchangeType.DIRECT, true);
        logger.info("创建直接交换机成功");
        return channel;
    }
}
