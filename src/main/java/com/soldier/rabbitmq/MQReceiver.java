package com.soldier.rabbitmq;

import com.soldier.domain.MiaoshaOrder;
import com.soldier.domain.MiaoshaUser;
import com.soldier.result.CodeMsg;
import com.soldier.service.GoodsService;
import com.soldier.service.MiaoshaService;
import com.soldier.service.OrderService;
import com.soldier.service.RedisService;
import com.soldier.util.JsonUtil;
import com.soldier.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MiaoshaService miaoshaService;

    /**
     * 第四步：请求出队，生产订单，减少库存   其它几步在MIaoshaController.class
     * 秒杀 Direct模式 交换机Exchange
     */
    @RabbitListener(queues = MQConfig.MIAOSHA_QUEUE)
    public void receiver(String message) {
        logger.info("receiver direct message:"+message);
        MiaoshaMessage miaoshaMessage = JsonUtil.strToBean(message, MiaoshaMessage.class);
        MiaoshaUser miaoshaUser = miaoshaMessage.getMiaoshaUser();
        Long goodsId = miaoshaMessage.getGoodsId();

        //判断库存
        GoodsVo goodsVo = goodsService.selectGoodsVoByGoodsId(goodsId);
        Integer stockCount = goodsVo.getStockCount();
        if (stockCount <= 0) {
            return;
        }

        //判断是否已经秒杀到了 防止重复购买(消息入队前，已经被判断了一次 MIaoshaController.class)
        MiaoshaOrder miaoshaOrder = orderService.selectMiaoshaOrderByMiaoshaUserIdAndGoodsId(miaoshaUser.getId(), goodsId);
        if (miaoshaOrder != null) {
            return;
        }

        //减库存 下订单 写入秒杀订单（三步必须放入一个事务里提交）
        miaoshaService.miaosha(miaoshaUser, goodsVo);
    }


//    /**
//     * Direct模式 交换机Exchange
//     */
//    @RabbitListener(queues = MQConfig.QUEUE_NAME)
//    public void receiver(String message) {
//        logger.info("receiver direct message:"+message);
//    }
//
//    /**
//     * Topic模式 交换机Exchange
//     */
//    @RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
//    public void receiverTopic1(String message) {
//        logger.info("receiver topic queue1 message:"+message);
//    }
//    @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
//    public void receiverTopic2(String message) {
//        logger.info("receiver topic queue2 message:"+message);
//    }
//
//    /**
//     * Headers模式 交换机Exchange
//     *  与其它的模式不同，接受的是字节数组
//     */
//    @RabbitListener(queues = MQConfig.HEADERS_QUEUE)
//    public void receiverHeaders(byte[] message) {
//        logger.info("receiver headers queue message:"+new String(message));
//    }
}
