package com.itguigu.rabbitmq.topic;

import com.itguigu.rabbitmq.util.RabbitmqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 *
 * @Author darren
 * @Date 2023/3/18 16:13
 */
public class TopicConsumer1 {

    private static Logger logger = LoggerFactory.getLogger(TopicConsumer1.class);

    public static void main(String[] args) throws IOException {
        Channel channel = RabbitmqUtils.channel;

        // deliverCallback 传递回调
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            logger.info("TopicConsumer1 deliverCallback message:{}",
                    new String(message.getBody(),"utf-8"));
        };

        // cancelCallback 取消回调
        CancelCallback cancelCallback = (consumerTag) -> {
            logger.info("TopicConsumer1 cancelCallback consumerTag:{}", consumerTag);
        };

        /**
         * 消费者消费消息
         * 1.消费哪个队列
         * 2.消费成功之后是否要自动应答 true 代表自动应答 false 手动应答
         * 3.消费者成功消费回的调
         * 4.消费者未成功消费的回调
         */
        channel.basicConsume(RabbitmqUtils.TOPIC_EXCHANGE_QUEUE_NAME1,
                true, deliverCallback, cancelCallback);
    }
}
