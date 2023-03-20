package com.itguigu.rabbitmq.dead;

import com.itguigu.rabbitmq.util.RabbitmqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 普通队列-消费者
 * @Author darren
 * @Date 2023/3/20 14:22
 */
public class OrdinaryConsumer1 {

    private static Logger logger = LoggerFactory.getLogger(OrdinaryConsumer1.class);

    public static void main(String[] args) throws IOException {
        Channel channel = RabbitmqUtils.channel;

        DeliverCallback deliverCallback = (consumerTag, message) ->{
            String msg = new String(message.getBody(), "UTF-8");
            // requeue 设置为 false 代表拒绝重新入队 该队列如果配置了死信交换机将发送到死信队列中
            // 拒绝info9的消息
            if ("info9".equals(msg)) {
                channel.basicReject(message.getEnvelope().getDeliveryTag(), false);
                logger.info("普通队列拒绝的消息是：{}", msg);
            } else {
                // 应答的消息
                channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
                logger.info("普通队列应答的消息是：{}", msg);
            }
        };
        CancelCallback cancelCallback = (consumerTag)->{
            logger.info(consumerTag + "消息被中断");
        };
        channel.basicConsume(
                RabbitmqUtils.NORMAL_EXCHANGE_QUEUE_NAME,
                false,
                deliverCallback,
                cancelCallback);
    }
}
