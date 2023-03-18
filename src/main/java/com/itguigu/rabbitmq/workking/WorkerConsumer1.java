package com.itguigu.rabbitmq.workking;

import com.itguigu.rabbitmq.util.RabbitmqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 工作模式消费者1
 * 预取值的意思是：
 *      允许的未确认消息的最大数量一旦数量达到配置的数量，
 *      RabbitMQ 将停止在通道上传递更多消息，
 *      除非至少有一个未处理的消息被确认
 *
 *
 * @author darren
 * @create 2021-11-07 9:43
 */
public class WorkerConsumer1 {
    private static Logger logger = LoggerFactory.getLogger(WorkerConsumer1.class);

    public static void main(String[] args) throws IOException, TimeoutException {
        logger.info("Worker01 start accept message... ");
        Channel channel = RabbitmqUtils.channel;

        // 设置不公平分发或预取值。0为公平分发，1为不公平分发，
        channel.basicQos(2);

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
         *
         * 1.消费哪个队列
         * 2.消费成功之后是否要自动应答 true 代表自动应答 false 手动应答
         * 3.消费者成功消费回的调
         * 4.消费者未成功消费的回调
         */
        channel.basicConsume(RabbitmqUtils.WORKING_QUEUE_NAME, false, deliverCallback, cancelCallback);
    }
}
