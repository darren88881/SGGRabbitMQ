package com.itguigu.rabbitmq.simple;

import com.itguigu.rabbitmq.util.RabbitmqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 简单模式-消费者-并自动应答
 * @author darren
 * @create 2021-11-06 17:51
 */
public class Consumer {

    private static Logger logger = LoggerFactory.getLogger(Consumer.class);
    
    public static void main(String [] args) throws IOException, TimeoutException {
        // 获取信道
        Channel channel = RabbitmqUtils.channel;

        logger.info("等待接收消息");
        //推送的消息如何进行消费的接口回调
        DeliverCallback deliverCallback = (consumerTag, message) ->{
            logger.info(new String(message.getBody()));
            RabbitmqUtils.closeChannel();
        };

        // cancelCallback 取消回调
        CancelCallback cancelCallback = (consumerTag) -> {
            logger.info(consumerTag + "消息被中断");
        };

        /**
         * 消费者消费消息
         * 1.消费哪个队列
         * 2.消费成功之后是否要自动应答 true 代表自动应答 false 手动应答
         * 3.消费者成功消费回的调
         * 4.消费者未成功消费的回调
         */
        channel.basicConsume(RabbitmqUtils.QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
