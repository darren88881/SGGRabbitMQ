package com.itguigu.rabbitmq.deadqueue;

import com.itguigu.rabbitmq.util.RabbitmqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 死信队列-生产者
 *
 *  业务：
 *  发10条消息，到固定长度的普通队列，普通队列长度为6
 *      将第9（info8）个消息设置一个过期时间，到达消息头部过期后自动进入死信队列，
 *      将第10（info9）个消息拒绝,拒绝后进入死信队列。
 *  最终：
 *     死信队列：983210
 *     普通队列：7654
 *
 *  注意：
 *      普通队列满了之后继续添加消息，前面的消息会挤出到死信队列中。如：
 *      队列长度为6，添加10个进去后，先进去的4个会到死信队列中，最后6个消息还在普通队列中。
 *
 *  发送的消息       普通队列的消息 -> 死信队列的消息
 *  9876543210  ->  987654       -> 3210
 *
 * @Author darren
 * @Date 2023/3/20 14:22
 */
public class DeadProducer {
    private static Logger logger = LoggerFactory.getLogger(DeadProducer.class);
    public static void main(String[] args) throws IOException {

        Channel channel = RabbitmqUtils.createDeadExchangeAndQueue();

        // 设置TTL过期
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().expiration("10000").build();
        // 发送消息
        for (int i=0; i< 10; i++) {
            String message = "info"+i;
            // 将9的消息设置过期时间
            if (i==8) {
                channel.basicPublish(
                        RabbitmqUtils.NORMAL_EXCHANGE_NAME,
                        RabbitmqUtils.NORMAL_EXCHANGE_ROUTING_KEY,
                        properties,
                        message.getBytes());
            } else {
                channel.basicPublish(
                        RabbitmqUtils.NORMAL_EXCHANGE_NAME,
                        RabbitmqUtils.NORMAL_EXCHANGE_ROUTING_KEY,
                        null,
                        message.getBytes());
            }
            logger.info("发送的消息内容为：{}",message);
        }

    }
}
