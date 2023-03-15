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
 * 扇出模式消费者1
 *
 * @author darren
 * @create 2021-11-15 22:20
 */
public class FanOutConsumer1 {

    private static Logger logger = LoggerFactory.getLogger(FanOutConsumer1.class);

    public static void main(String [] args) throws IOException, TimeoutException {

        logger.info("FanOutConsumer1 begin...");

        Channel channel = RabbitmqUtils.getFanOutExchangeChannel();
        String queue1 = channel.queueDeclare().getQueue();
        channel.queueBind(queue1, RabbitmqUtils.EXCHANGE_NAME, "");

        // deliverCallback 传递回调
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            logger.info("FanOutConsumer1 deliverCallback message:{}", new String(message.getBody()));
        };

        // cancelCallback 取消回调
        CancelCallback cancelCallback = (consumerTag) -> {
            logger.info("FanOutConsumer1 cancelCallback consumerTag:{}", consumerTag);
        };

        channel.basicConsume(queue1, deliverCallback, cancelCallback);


    }
}
