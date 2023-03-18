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
    public  static String   QUEUE_NAME                   = "hello";
    public  static String   WORKING_QUEUE_NAME           = "working_queue";

    public  static String   FANOUT_EXCHANGE_NAME                 = "fanout_exchange";
    public  static String   FANOUT_EXCHANGE_QUEUE_NAME1          = "fanout_exchange_queue1";
    public  static String   FANOUT_EXCHANGE_QUEUE_NAME2          = "fanout_exchange_queue2";

    public  static String   DIRECT_EXCHANGE_NAME          = "direct_exchange";
    public  static String   DIRECT_EXCHANGE_QUEUE_NAME1   = "direct_exchange_queue1";
    public  static String   DIRECT_EXCHANGE_QUEUE_NAME2   = "direct_exchange_queue2";
    public  static String   DIRECT_EXCHANGE_ROUTING_KEY1  = "direct_routing_key_1";
    public  static String   DIRECT_EXCHANGE_ROUTING_KEY2  = "direct_routing_key_2";

    public  static String   TOPIC_EXCHANGE_NAME          = "topic_exchange";
    public  static String   TOPIC_EXCHANGE_QUEUE_NAME1   = "topic_exchange_queue1";
    public  static String   TOPIC_EXCHANGE_QUEUE_NAME2   = "topic_exchange_queue2";
    public  static String   TOPIC_EXCHANGE_ROUTING_KEY1  = "topic_routing_key_1";
    public  static String   TOPIC_EXCHANGE_ROUTING_KEY2  = "topic_routing_key_2";



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
     * 简单模式-创建队列
     * @return
     * @throws IOException
     */
    public static Channel createSimpleQueue() throws IOException {
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
        return channel;
    }

    /**
     * 工作模式-创建队列
     * @return
     * @throws IOException
     */
    public static Channel createWorkingQueue() throws IOException {
        channel.queueDeclare(RabbitmqUtils.WORKING_QUEUE_NAME, RabbitmqUtils.DURABLE, false, false, null);
        return channel;
    }


    /**
     * 扇出模式-创建交换机和队列
     * @return
     * @throws IOException
     * @throws TimeoutException
     */
    public static Channel createFanOutExchangeAndQueue() throws IOException {

        // 删除名为 fanout_exchange 的交换机
        channel.exchangeDelete(RabbitmqUtils.FANOUT_EXCHANGE_NAME);
        /**
         * 创建交换机
         * exchange: 交换机名称,
         * BuiltinExchangeType：交换机类型,
         * durable: 持久化交换机
         */
        channel.exchangeDeclare(RabbitmqUtils.FANOUT_EXCHANGE_NAME, BuiltinExchangeType.FANOUT, true);

        // 创建队列1
        channel.queueDeclare(RabbitmqUtils.FANOUT_EXCHANGE_QUEUE_NAME1, true, false, false, null);
        // 队列1和交换机绑定
        channel.queueBind(RabbitmqUtils.FANOUT_EXCHANGE_QUEUE_NAME1, RabbitmqUtils.FANOUT_EXCHANGE_NAME, "");

        // 创建队列2
        channel.queueDeclare(RabbitmqUtils.FANOUT_EXCHANGE_QUEUE_NAME2, true, false, false, null);
        // 队列2和交换机绑定
        channel.queueBind(RabbitmqUtils.FANOUT_EXCHANGE_QUEUE_NAME2, RabbitmqUtils.FANOUT_EXCHANGE_NAME, "");

        logger.info("创建扇出交换机和队列并绑定成功");
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

        // 创建队列1
        channel.queueDeclare(RabbitmqUtils.DIRECT_EXCHANGE_QUEUE_NAME1,
                true, false, false, null);
        // 队列1和信道绑定,并指定routingKey
        channel.queueBind(
                RabbitmqUtils.DIRECT_EXCHANGE_QUEUE_NAME1,
                RabbitmqUtils.DIRECT_EXCHANGE_NAME,
                RabbitmqUtils.DIRECT_EXCHANGE_ROUTING_KEY1);

        // 创建队列2
        channel.queueDeclare(RabbitmqUtils.DIRECT_EXCHANGE_QUEUE_NAME2,
                true, false, false, null);
        // 队列2和信道绑定,并指定routingKey
        channel.queueBind(
                RabbitmqUtils.DIRECT_EXCHANGE_QUEUE_NAME2,
                RabbitmqUtils.DIRECT_EXCHANGE_NAME,
                RabbitmqUtils.DIRECT_EXCHANGE_ROUTING_KEY2);

        logger.info("创建直接交换机成功");
        return channel;
    }

    /**
     * 创建主题交换机
     * @return
     */
    public static Channel createTopicExchangeChannel() throws IOException {
        // 删除交换机
        channel.exchangeDelete(RabbitmqUtils.TOPIC_EXCHANGE_NAME);
        // 创建交换机
        channel.exchangeDeclare(RabbitmqUtils.TOPIC_EXCHANGE_NAME, BuiltinExchangeType.TOPIC, true);

        // 创建队列1
        channel.queueDeclare(RabbitmqUtils.TOPIC_EXCHANGE_QUEUE_NAME1,
                true, false,false,null);
        // 绑定队列1并设置routingKey
        channel.queueBind(
                RabbitmqUtils.TOPIC_EXCHANGE_QUEUE_NAME1,
                RabbitmqUtils.TOPIC_EXCHANGE_NAME, "*.orange.*");

        // 创建队列2
        channel.queueDeclare(RabbitmqUtils.TOPIC_EXCHANGE_QUEUE_NAME2,
                true, false,false,null);
        // 绑定队列2到交换机上并设置routingKey
        channel.queueBind(
                RabbitmqUtils.TOPIC_EXCHANGE_QUEUE_NAME2,
                RabbitmqUtils.TOPIC_EXCHANGE_NAME, "*.*.rabbit");
        // 绑定队列2到交换机上并设置routingKey
        channel.queueBind(
                RabbitmqUtils.TOPIC_EXCHANGE_QUEUE_NAME2,
                RabbitmqUtils.TOPIC_EXCHANGE_NAME, "lazy.#");
        logger.info("创建主题交换机成功");
        return channel;
    }
}
