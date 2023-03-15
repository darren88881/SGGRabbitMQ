package com.itguigu.rabbitmq.fanout;

import com.itguigu.rabbitmq.util.RabbitmqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * 扇出模式生产者
 * 类似于群消息，一人发多人收
 *
 * @author darren
 * @create 2021-11-15 21:21
 */
public class ProducerFanOut {

    private static Logger logger = LoggerFactory.getLogger(ProducerFanOut.class);

    public static void main(String [] args) throws IOException, TimeoutException {
        logger.info("fanout begin...");
        Channel channel = RabbitmqUtils.getFanOutExchangeChannel();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            /**
             * String exchange              : 交换机名称
             * String routingKey            ：路由密钥
             * BasicProperties props        ：消息配置
             *      TEXT_PLAIN              ：纯文本
             *      PERSISTENT_TEXT_PLAIN   ：持久纯文本
             * byte[] body                  ：消息体
             */
            channel.basicPublish(RabbitmqUtils.EXCHANGE_NAME, "", MessageProperties.TEXT_PLAIN, message.getBytes());
            logger.info("message :" + message);
        }
    }
}
