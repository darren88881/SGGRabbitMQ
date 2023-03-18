package com.itguigu.rabbitmq.fanout;


import com.itguigu.rabbitmq.util.RabbitmqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 扇出模式消费者2
 *
 * @author darren
 * @create 2021-11-15 22:20
 */
public class FanOutConsumer2 {

    private static Logger logger = LoggerFactory.getLogger(FanOutConsumer2.class);

    public static void main(String [] args) throws IOException, TimeoutException {
        logger.info("FanOutConsumer2 begin...");
        Channel channel = RabbitmqUtils.channel;

        channel.queueDeclare(RabbitmqUtils.FANOUT_EXCHANGE_QUEUE_NAME2, true, false, false, null);

        channel.queueBind(RabbitmqUtils.FANOUT_EXCHANGE_QUEUE_NAME2, RabbitmqUtils.FANOUT_EXCHANGE_NAME, "");

        // deliverCallback 传递回调
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            logger.info("FanOutConsumer2 deliverCallback message:{}", new String(message.getBody()));
        };

        // cancelCallback 取消回调
        CancelCallback cancelCallback = (consumerTag) -> {
            logger.info("FanOutConsumer2 cancelCallback consumerTag:{}", consumerTag);
        };

        channel.basicConsume(RabbitmqUtils.FANOUT_EXCHANGE_QUEUE_NAME2, deliverCallback, cancelCallback);
    }
}
