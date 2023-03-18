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
 * 直接模式消费者1
 *
 * @author darren
 * @create 2021-11-15 22:20
 */
public class DirectConsumer1 {

    private static Logger logger = LoggerFactory.getLogger(DirectConsumer1.class);

    public static void main(String [] args) throws IOException, TimeoutException {

        logger.info("DirectConsumer1 begin...");
        // 获取信道
        Channel channel = RabbitmqUtils.channel;

        // 创建队列
        channel.queueDeclare(RabbitmqUtils.DIRECT_EXCHANGE_QUEUE_NAME1,
                true, false, false, null);
        // 队列和信道绑定,并指定routingKey
        channel.queueBind(
                RabbitmqUtils.DIRECT_EXCHANGE_QUEUE_NAME1,
                RabbitmqUtils.DIRECT_EXCHANGE_NAME,
                RabbitmqUtils.DIRECT_EXCHANGE_ROUTING_KEY1);

        // deliverCallback 传递回调
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            logger.info("DirectConsumer1 deliverCallback message:{}", new String(message.getBody()));
        };

        // cancelCallback 取消回调
        CancelCallback cancelCallback = (consumerTag) -> {
            logger.info("DirectConsumer1 cancelCallback consumerTag:{}", consumerTag);
        };

        /**
         * 消费者消费消息
         *
         * 1.消费哪个队列
         * 2.消费成功之后是否要自动应答 true 代表自动应答 false 手动应答
         * 3.消费者成功消费回的调
         * 4.消费者未成功消费的回调
         */
        channel.basicConsume(RabbitmqUtils.DIRECT_EXCHANGE_QUEUE_NAME1, true,
                deliverCallback, cancelCallback);


    }
}
