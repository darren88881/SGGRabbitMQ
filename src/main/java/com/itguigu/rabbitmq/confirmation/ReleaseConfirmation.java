package com.itguigu.rabbitmq.confirmation;

import com.itguigu.rabbitmq.util.RabbitmqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeoutException;

/**
 * 发布确认模式
 *
 * @author darren
 * @create 2021-11-13 17:02
 */
public class ReleaseConfirmation {

    public static Logger logger =   LoggerFactory.getLogger(ReleaseConfirmation.class);

    public static void main(String [] args) throws InterruptedException, TimeoutException, IOException {
        // 单个发布确认模式:发布1000个消息耗时：555ms
        publishMessageIndividual();
        // 批量发布确认模式:发布1000个消息耗时：66ms
        // publishMessageBatch();
        // 异步发布确认模式:发布1000个消息耗时：28ms
         publishMessageAync();


    }

    /**
     * 单个发布确认模式:发布1000个消息耗时：555ms
     *
     * @throws IOException
     * @throws TimeoutException
     * @throws InterruptedException
     */
    public static void publishMessageIndividual() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitmqUtils.getChannel();
        // 因为队列经过持久化后该队列存在所以再次创建后报错，直接使用已存在的队列即可

        // 开启发布确认模式
        channel.confirmSelect();
        long begin = System.currentTimeMillis();

        for (int i = 0; i < RabbitmqUtils.MESSAGE_NUM; i++) {
            String message = i+"";
            channel.basicPublish("", RabbitmqUtils.QUEUE_NAME, null, message.getBytes());
            // 确认发送
            boolean flag = channel.waitForConfirms();
            if (flag) {
                logger.info(i+"消息发送成功");
            }
        }

        long end = System.currentTimeMillis();

        logger.info("发布"+ RabbitmqUtils.MESSAGE_NUM + "个消息耗时："+ (end - begin)+"ms" );
    }

    /**
     * 批量发布确认模式:发布1000个消息耗时：66ms
     *
     * @throws IOException
     * @throws TimeoutException
     * @throws InterruptedException
     */
    public static void publishMessageBatch() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitmqUtils.getChannel();

        channel.confirmSelect();
        long begin = System.currentTimeMillis();

        for (int i = 0; i < RabbitmqUtils.MESSAGE_NUM; i++) {
            String message = i+"";
            channel.basicPublish("", RabbitmqUtils.QUEUE_NAME, null, message.getBytes());
            if (i % 100 == 0) {
                channel.waitForConfirms();
                logger.info(i+"消息发送成功");
            }
        }

        long end = System.currentTimeMillis();

        logger.info("发布"+ RabbitmqUtils.MESSAGE_NUM + "个消息耗时："+ (end - begin)+"ms" );
    }

    /**
     * 异步发布确认模式:发布1000个消息耗时：28ms
     *
     * @throws IOException
     * @throws TimeoutException
     * @throws InterruptedException
     */
    public static void publishMessageAync() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitmqUtils.getChannel();

        channel.confirmSelect();

        ConcurrentSkipListMap<Long, String> skipListMap = new ConcurrentSkipListMap<>();

        long begin = System.currentTimeMillis();

        /**
         * 成功消息
         * deliveryTag： 消息编号
         * multiple：    是否批量
         */
        ConfirmCallback ackCallback = (deliveryTag, multiple) -> {
            if (multiple) {
                // 是批量就批量清理
                ConcurrentNavigableMap<Long, String> headMap = skipListMap.headMap(deliveryTag);
                logger.info("批量已确认消息是："+ headMap.toString());
                headMap.clear();
            } else {
                // 不是批量就删除当前的消息
                String message = skipListMap.get(deliveryTag);
                logger.info("非批量已确认消息是："+ message);
                skipListMap.remove(deliveryTag);
            }
        };

        // 失败消息
        ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
            String message = skipListMap.get(deliveryTag);
            logger.info("未确认消息是："+ message);
            logger.info("未确认消息有："+ skipListMap.size()+" 条");
        };

        // 消息监听器
        channel.addConfirmListener(ackCallback, nackCallback);

        for (int i = 0; i < RabbitmqUtils.MESSAGE_NUM; i++) {
            String message = i+"";
            channel.basicPublish("", RabbitmqUtils.QUEUE_NAME, null, message.getBytes());
            // 将消息放入并发跳跃表中
            skipListMap.put(Long.parseLong(String.valueOf(i)), message);
        }

        long end = System.currentTimeMillis();

        logger.info("发布"+ RabbitmqUtils.MESSAGE_NUM +"个消息耗时："+ (end - begin)+"ms" );

    }
}
