package com.soldier.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * @Author soldier
 * @Date 20-4-24 下午3:48
 * @Email:583406411@qq.com
 * @Version 1.0
 * @Description:接收器-四种交换机模式
 */
@Service
public class MQReceiver {

    private static Logger logger = LoggerFactory.getLogger(MQReceiver.class);

    /**
     * Direct模式 交换机Exchange
     */
    @RabbitListener(queues = MQConfig.QUEUE_NAME)
    public void receiver(String message) {
        logger.info("receiver direct message:"+message);
    }

    /**
     * Topic模式 交换机Exchange
     */
    @RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
    public void receiverTopic1(String message) {
        logger.info("receiver topic queue1 message:"+message);
    }
    @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
    public void receiverTopic2(String message) {
        logger.info("receiver topic queue2 message:"+message);
    }

    /**
     * Headers模式 交换机Exchange
     *  与其它的模式不同，接受的是字节数组
     */
    @RabbitListener(queues = MQConfig.HEADERS_QUEUE)
    public void receiverHeaders(byte[] message) {
        logger.info("receiver headers queue message:"+new String(message));
    }
}
