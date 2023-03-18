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
        // 获取信道
        Channel channel = RabbitmqUtils.channel;

        // 创建队列
        channel.queueDeclare(RabbitmqUtils.EXCHANGE_QUEUE_NAME1, true, false, false, null);
        // 队列和信道绑定
        channel.queueBind(RabbitmqUtils.EXCHANGE_QUEUE_NAME1, RabbitmqUtils.EXCHANGE_NAME, "");

        // deliverCallback 传递回调
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            logger.info("FanOutConsumer1 deliverCallback message:{}", new String(message.getBody()));
        };

        // cancelCallback 取消回调
        CancelCallback cancelCallback = (consumerTag) -> {
            logger.info("FanOutConsumer1 cancelCallback consumerTag:{}", consumerTag);
        };

        channel.basicConsume(RabbitmqUtils.EXCHANGE_QUEUE_NAME1, deliverCallback, cancelCallback);


    }
}
