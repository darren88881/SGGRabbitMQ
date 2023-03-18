package com.itguigu.rabbitmq.direct;

import com.itguigu.rabbitmq.util.RabbitmqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * 直接模式
 * direct:一人发指定谁收谁就收
 *
 * 生产者
 * @author darren
 * @create 2021-11-15 21:21
 */
public class DirectProducer {

    private static Logger logger = LoggerFactory.getLogger(DirectProducer.class);

    public static void main(String [] args) throws IOException, TimeoutException {
        logger.info("direct begin...");
        Channel channel = RabbitmqUtils.createDirectExchangeChannel();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            /**
             * 发送消息
             *
             * String exchange              : 交换机名称
             * String routingKey            ：路由密钥
             * BasicProperties props        ：消息配置
             *      TEXT_PLAIN              ：纯文本
             *      PERSISTENT_TEXT_PLAIN   ：持久纯文本
             * byte[] body                  ：消息体
             */
            channel.basicPublish(
                    RabbitmqUtils.DIRECT_EXCHANGE_NAME,
                    RabbitmqUtils.DIRECT_EXCHANGE_ROUTING_KEY2,
                    MessageProperties.TEXT_PLAIN,
                    message.getBytes());
            logger.info("message :" + message);
        }
    }
}
