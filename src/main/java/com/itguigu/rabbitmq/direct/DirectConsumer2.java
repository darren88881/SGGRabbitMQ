package com.itguigu.rabbitmq.direct;


import com.itguigu.rabbitmq.util.RabbitmqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 直接模式消费者2
 *
 * @author darren
 * @create 2021-11-15 22:20
 */
public class DirectConsumer2 {

    private static Logger logger = LoggerFactory.getLogger(DirectConsumer2.class);

    public static void main(String [] args) throws IOException, TimeoutException {
        logger.info("DirectConsumer2 begin...");
        Channel channel = RabbitmqUtils.channel;

        // deliverCallback 传递回调
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            logger.info("DirectConsumer2 deliverCallback message:{}", new String(message.getBody()));
        };

        // cancelCallback 取消回调
        CancelCallback cancelCallback = (consumerTag) -> {
            logger.info("DirectConsumer2 cancelCallback consumerTag:{}", consumerTag);
        };

        /**
         * 消费者消费消息
         *
         * 1.消费哪个队列
         * 2.消费成功之后是否要自动应答 true 代表自动应答 false 手动应答
         * 3.消费者成功消费回的调
         * 4.消费者未成功消费的回调
         */
        channel.basicConsume(RabbitmqUtils.DIRECT_EXCHANGE_QUEUE_NAME2, true,
                deliverCallback, cancelCallback);
    }
}
