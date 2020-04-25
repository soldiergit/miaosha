package com.soldier.controller;

import com.soldier.access.AccessLimit;
import com.soldier.domain.MiaoshaOrder;
import com.soldier.domain.MiaoshaUser;
import com.soldier.domain.OrderInfo;
import com.soldier.rabbitmq.MQSender;
import com.soldier.rabbitmq.MiaoshaMessage;
import com.soldier.redis.prefix.GoodsHtmlKey;
import com.soldier.result.CodeMsg;
import com.soldier.result.Result;
import com.soldier.service.*;
import com.soldier.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author soldier
 * @Date 20-4-21 上午9:08
 * @Email:583406411@qq.com
 * @Version 1.0
 * @Description:
 */
@Controller
@RequestMapping("/miaosha")
public class MIaoshaController implements InitializingBean {

    @Autowired
    private MiaoshaUserService miaoshaUserService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MiaoshaService miaoshaService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private MQSender orderMQSender;

    //商品库存标记 减少redis访问
    private Map<Long, Boolean> localOverMap = new HashMap<>();

    /*弃用，使用rabbitmq优化接口
    @RequestMapping("/do_miaosha")
    public String miaosha(@RequestParam("goodsId") Long goodsId, MiaoshaUser miaoshaUser, Model model){

        if (miaoshaUser == null) {
            return "login";
        }

        //判断库存
        GoodsVo goodsVo = goodsService.selectGoodsVoByGoodsId(goodsId);
        Integer stockCount = goodsVo.getStockCount();
        if (stockCount <= 0) {
            model.addAttribute("errmsg", CodeMsg.MIAO_SHA_OVER);
            return "miaosha_fail";
        }

        //判断是否已经秒杀到了
        MiaoshaOrder miaoshaOrder = orderService.selectMiaoshaOrderByMiaoshaUserIdAndGoodsId(miaoshaUser.getId(), goodsId);
        if (miaoshaOrder != null) {
            model.addAttribute("errmsg", CodeMsg.REPEATE_MIAOSHA);
            return "miaosha_fail";
        }

        //减库存 下订单 写入秒杀订单（三步必须放入一个事务里提交）
        OrderInfo orderInfo = miaoshaService.miaosha(miaoshaUser, goodsVo);
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("goodsVo", goodsVo);

        return "order_detail";
    }*/

    /**
     * 第一步：系统初始化时，把商品库存数量加载到redis
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVoList = goodsService.listGoodsVo();
        for (GoodsVo goodsVo : goodsVoList) {
            //商品库存标记 减少redis访问
            this.localOverMap.put(goodsVo.getId(), false);
            redisService.set(GoodsHtmlKey.getMiaoshaGoodsStock, "" + goodsVo.getId(), goodsVo.getStockCount());
        }
    }

    /**
     * 使用rabbitmq优化接口
     * 安全优化 接口地址隐藏
     */
    @RequestMapping(value = "/{path}/do_miaosha", method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> miaosha(@RequestParam("goodsId") Long goodsId,
                                   @PathVariable("path") String path,
                                   MiaoshaUser miaoshaUser, Model model) {

        if (miaoshaUser == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        //验证path
        boolean check = miaoshaService.checkPath(miaoshaUser, goodsId, path);
        if(!check){
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }

        //商品库存标记 减少redis访问
        Boolean over = this.localOverMap.get(goodsId);
        if (over) {
            return Result.error(CodeMsg.MIAOSHA_FAIL);
        }
        //第二步：redis预减库存
        Long stock = redisService.decr(GoodsHtmlKey.getMiaoshaGoodsStock, "" + goodsId);
        // 库存不足
        if (stock < 0) {
            this.localOverMap.put(goodsId, true);
            return Result.error(CodeMsg.MIAOSHA_FAIL);
        }

        //判断是否已经秒杀到了 防止重复购买(消息被读取时，会再次判断 MQReceiver.class)
        MiaoshaOrder miaoshaOrder = orderService.selectMiaoshaOrderByMiaoshaUserIdAndGoodsId(miaoshaUser.getId(), goodsId);
        if (miaoshaOrder != null) {
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }

        //第三步：请求入队(消息队列)，立即返回信息：“排队中”
        MiaoshaMessage miaoshaMessage = new MiaoshaMessage();
        miaoshaMessage.setMiaoshaUser(miaoshaUser);
        miaoshaMessage.setGoodsId(goodsId);
        // 第四步在MQReceiver.class
        orderMQSender.sendMiaoshaMessage(miaoshaMessage);
        //排队中
        return Result.success(0);
    }

    /**
     * 第五步：客户端轮询，是否秒杀成功
     * orderId：成功
     * -1：秒杀失败
     * 0： 排队中
     */
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> miaoshaResult(Model model, MiaoshaUser miaoshaUser,
                                      @RequestParam("goodsId") long goodsId) {
        if (miaoshaUser == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        model.addAttribute("miaoshaUser", miaoshaUser);

        // 判断用户是否秒杀到商品，即判断订单是否存在
        long result = miaoshaService.getMiaoshaResult(miaoshaUser.getId(), goodsId);
        return Result.success(result);
    }

    /**
     * 校验图片验证码并获取秒杀地址
     * AccessLimit 接口的限流防刷
     */
    @AccessLimit(seconds = 5, maxCount = 5, needLogin = true)
    @RequestMapping(value = "/getPath", method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getMiaoshaPath(HttpServletRequest request, MiaoshaUser user,
                                         @RequestParam("goodsId") long goodsId,
                                         @RequestParam(value = "verifyCode", defaultValue = "0") int verifyCode
    ) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        // 校验 验证码
        boolean check = miaoshaService.checkVerifyCode(user, goodsId, verifyCode);
        if (!check) {
            return Result.error(CodeMsg.VERIFY_CODE_ERROR);
        }
        String path = miaoshaService.createMiaoshaPath(user, goodsId);
        return Result.success(path);
    }

    /**
     * 生成验证码
     * @param response
     * @param user
     * @param goodsId
     * @return 什么都不用返回，因为这样子已经通过response的OutputStream返回出去了
     */
    @RequestMapping(value = "/verifyCode", method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getMiaoshaVerifyCod(HttpServletResponse response, MiaoshaUser user,
                                              @RequestParam("goodsId") long goodsId) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        try {
            // 生成图片验证码
            BufferedImage image = miaoshaService.createVerifyCode(user, goodsId);
            // 获取输出流
            OutputStream out = response.getOutputStream();
            // 往客户端写入图片
            ImageIO.write(image, "JPEG", out);
            out.flush();
            out.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(CodeMsg.GET_VERIFY_CODE);
        }
    }
}
