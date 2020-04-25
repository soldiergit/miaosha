package com.soldier.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author soldier
 * @Date 20-4-24 下午3:48
 * @Email:583406411@qq.com
 * @Version 1.0
 * @Description:四种交换机模式
 */
@Configuration
public class MQConfig {

    //秒杀
    public static final String MIAOSHA_QUEUE = "miaosha.queue";


    public static final String QUEUE_NAME = "queue";
    public static final String TOPIC_QUEUE1 = "topic.queue1";
    public static final String TOPIC_QUEUE2 = "topic.queue2";
    public static final String HEADERS_QUEUE = "headers.queue";
    public static final String TOPIC_EXCHANGE = "topicExchange";
    public static final String FANOUT_EXCHANGE = "fanoutExchange";
    public static final String HEADERS_EXCHANGE = "headersExchange";

    /**
     * 秒杀订单 Direct模式 交换机Exchange
     */
    @Bean
    public Queue miaoshaQueue() {
        return new Queue(MIAOSHA_QUEUE, true);
    }

    /**
     * Direct模式 交换机Exchange
     */
    @Bean
    public Queue directQueue() {
        return new Queue(QUEUE_NAME, true);
    }

    /**
     * Topic模式 交换机Exchange
     *  主题模式
     *      生产者发送消息到交换机；
     *      交换机消息送达队列为topic方式；
     *      消费者模糊匹配路由Key，进行消息消费；
     *      #号 匹配一个或多个
     *      *号 匹配一个
     * 一般使用#号匹配多个，*号用的比较少
     * 这里给这个交换机绑定了两个queue
     */
    @Bean
    public Queue topicQueue1() {
        return new Queue(TOPIC_QUEUE1, true);
    }
    @Bean
    public Queue topicQueue2() {
        return new Queue(TOPIC_QUEUE2, true);
    }
    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(TOPIC_EXCHANGE);
    }
    @Bean
    public Binding topicBinding1() {
        return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with("topic.key1");
    }
    @Bean
    public Binding topicBinding2() {
        return BindingBuilder.bind(topicQueue2()).to(topicExchange()).with("topic.#");
    }

    /**
     * Fanout模式 交换机Exchange
     *  广播模式，发给多个queue
     *      这里给这个交换机绑定了两个queue
     */
    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(FANOUT_EXCHANGE);
    }
    @Bean
    public Binding fanoutBinding1() {
        return BindingBuilder.bind(topicQueue1()).to(fanoutExchange());
    }
    @Bean
    public Binding fanoutBinding2() {
        return BindingBuilder.bind(topicQueue2()).to(fanoutExchange());
    }

    /**
     * Headers模式 交换机Exchange
     *  headers与direct的模式不同，不是使用routingkey去做绑定。而是通过消息headers的键值对匹配
     *  发布者  -- >  headersExchange  -->  (key: value) binding  --> queue
     */
    @Bean
    public Queue headersQueue() {
        return new Queue(HEADERS_QUEUE, true);
    }
    @Bean
    public HeadersExchange headersExchange(){
        return new HeadersExchange(HEADERS_EXCHANGE);
    }
    @Bean
    public Binding headersBinding() {
        Map<String, Object> map = new HashMap<>();
        map.put("header1", "value1");
        map.put("header2", "value2");
        //只有当满足map中的key-value才会往指定的queue里放东西
        return BindingBuilder.bind(headersQueue()).to(headersExchange()).whereAll(map).match();
    }
}
