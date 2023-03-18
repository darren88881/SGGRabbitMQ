package com.itguigu.rabbitmq.workking;

import com.itguigu.rabbitmq.util.RabbitmqUtils;
import com.itguigu.rabbitmq.util.ThreadUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author darren
 * @create 2021-11-07 9:43
 */
public class Worker02 {

    private static Logger logger = LoggerFactory.getLogger(Worker02.class);

    public static void main(String[] args) throws IOException, TimeoutException {
        logger.info("Worker02 start accept message... ");
        Channel channel = RabbitmqUtils.channel;
        // 设置不公平分发或预取值。0为公平分发，1为不公平分发，大于1为预取值
        channel.basicQos(5);

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String messageStr = new String(message.getBody());
            ThreadUtils.sleep(10);

            // 手动应答
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
            logger.info("Worker02 accept message is messageStr:{}", messageStr);
        };

        CancelCallback cancelCallback = (consumerTag) -> {
            logger.info("Worker02 fail message is consumerTag:{}", consumerTag);
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
