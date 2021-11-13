package com.itgu.rabbitma.workking;

import com.itgu.rabbitmq.util.RabbitmqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 工作模式消费者1
 *
 * @author darren
 * @create 2021-11-07 9:43
 */
public class Worker01 {
    private static Logger logger = LoggerFactory.getLogger(Worker01.class);

    public static void main(String[] args) throws IOException, TimeoutException {
        logger.info("Worker01 start accept message... ");
        Channel channel = RabbitmqUtils.getChannel();

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String messageStr = new String(message.getBody());
            /**
             * 1.消息标记tag
             * 2.是否批量应答未应答的消息
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
            logger.info("Worker01 accept message is messageStr:{}", messageStr);
        };

        CancelCallback cancelCallback = (consumerTag) -> {
            logger.info("Worker01 fail message is consumerTag:{}", consumerTag);
        };

        /**
         * 消费者消费消息
         * 1.消费哪个队列
         * 2.消费成功之后是否要自动应答 true 代表自动应答 false 手动应答
         * 3.消费者成功消费回的调
         * 4.消费者未成功消费的回调
         */
        channel.basicConsume(RabbitmqUtils.QUEUE_NAME, false, deliverCallback, cancelCallback);
    }
}
