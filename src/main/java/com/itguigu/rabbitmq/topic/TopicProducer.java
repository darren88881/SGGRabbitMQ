package com.itguigu.rabbitmq.topic;

import com.itguigu.rabbitmq.util.RabbitmqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;

/**
 * 主题模式
 * topic:一人发谁匹配谁收
 *
 * @Author darren
 * @Date 2023/3/18 16:13
 */
public class TopicProducer {

    private static Logger logger = LoggerFactory.getLogger(TopicProducer.class);

    public static void main(String[] args) throws IOException {
        Channel channel = RabbitmqUtils.createTopicExchangeChannel();

        HashMap<String, String> bindingKeyMap = new HashMap<>();
        bindingKeyMap.put("quick.orange.rabbit","被队列 Q1Q2 接收到");
        bindingKeyMap.put("lazy.orange.elephant","被队列 Q1Q2 接收到");
        bindingKeyMap.put("quick.orange.fox","被队列 Q1 接收到");
        bindingKeyMap.put("lazy.brown.fox","被队列 Q2 接收到");
        bindingKeyMap.put("lazy.pink.rabbit","虽然满足两个绑定但只被队列 Q2 接收一次");
        bindingKeyMap.put("quick.brown.fox","不匹配任何绑定不会被任何队列接收到会被丢弃");
        bindingKeyMap.put("quick.orange.male.rabbit","是四个单词不匹配任何绑定会被丢弃");
        bindingKeyMap.put("lazy.orange.male.rabbit","是四个单词但匹配 Q2");

        bindingKeyMap.forEach((key, value)->{
            try {
                channel.basicPublish(RabbitmqUtils.TOPIC_EXCHANGE_NAME, key,
                        MessageProperties.TEXT_PLAIN,value.getBytes("UTF-8"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        logger.info("消息发送成功");
    }
}
