package com.itguigu.rabbitmq.deadqueue;

import com.itguigu.rabbitmq.util.RabbitmqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 死信队列-消费者
 * @Author darren
 * @Date 2023/3/20 14:22
 */
public class DeadConsumer2 {

    private static Logger logger = LoggerFactory.getLogger(DeadConsumer2.class);

    public static void main(String[] args) throws IOException {
        Channel channel = RabbitmqUtils.channel;

        DeliverCallback deliverCallback = (consumerTag, message) ->{
            String msg = new String(message.getBody(), "UTF-8");
            logger.info("死信队列接受到的消息是：{}", msg);
        };
        CancelCallback cancelCallback = (consumerTag)->{
            logger.info(consumerTag + "消息被中断");
        };
        channel.basicConsume(
                RabbitmqUtils.DEAD_EXCHANGE_QUEUE_NAME,
                true,
                deliverCallback,
                cancelCallback);
    }
}
