package com.soldier.rabbitmq;

import com.soldier.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author soldier
 * @Date 20-4-24 下午3:48
 * @Email:583406411@qq.com
 * @Version 1.0
 * @Description:发件人-四种交换机模式
 */
@Service
public class MQSender {

    private static Logger logger = LoggerFactory.getLogger(MQSender.class);

    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 请求入队(消息队列)，立即返回信息：“排队中”
     */
    public void sendMiaoshaMessage(MiaoshaMessage miaoshaMessage) {
        String message = JsonUtil.beanToStr(miaoshaMessage);
        logger.info("send message "+message);
        amqpTemplate.convertAndSend(MQConfig.MIAOSHA_QUEUE, message);
    }

//    *
//     * Direct模式
//     * 发送消息时只需要指定Queue队列名称，和消息
//
//    public void sendDirect(Object message) {
//
//        String msg = JsonUtil.beanToStr(message);
//
//        logger.info("send direct message:"+msg);
//
//        amqpTemplate.convertAndSend(MQConfig.QUEUE_NAME, msg);
//    }
//
//    *
//     * Topic模式
//     * 发送消息时需要指定交换器Exchange，routingKey，和消息
//
//    public void sendTopic(Object message) {
//
//        String msg = JsonUtil.beanToStr(message);
//
//        logger.info("send topic message:"+msg);
//
//        //匹配得上routingKey=topic.key2
//        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key2", msg+"1");
//        //使用通配符=topic.#
//        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key2", msg+"2");
//    }
//
//    *
//     * Fanout模式
//     * 发送消息时需要指定交换器Exchange，空的routingKey，消息
//
//    public void sendFanout(Object message) {
//
//        String msg = JsonUtil.beanToStr(message);
//
//        logger.info("send fanout message:"+msg);
//
//        amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE, "", msg+"1");
//    }
//
//    *
//     * Headers模式
//     * 发送消息时需要指定交换器Exchange，空的routingKey，Message对象
//
//    public void sendHeaders(Object message) {
//
//        String msg = JsonUtil.beanToStr(message);
//
//        logger.info("send headers message:"+msg);
//
//        MessageProperties messageProperties = new MessageProperties();
//        //必须与MQConfig中指定的map的key-value一样
//        messageProperties.setHeader("header1", "value1");
//        messageProperties.setHeader("header2", "value2");
//        Message obj = new Message(msg.getBytes(), messageProperties);
//        amqpTemplate.convertAndSend(MQConfig.HEADERS_EXCHANGE, "", obj);
//    }
}
