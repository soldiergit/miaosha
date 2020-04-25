package com.soldier.util;

import com.soldier.vo.VerifyCodeVo;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * @Author soldier
 * @Date 20-4-25 下午1:32
 * @Email:583406411@qq.com
 * @Version 1.0
 * @Description:验证码工具类
 */
public class VerifyCodeUtil {

    // 数学运算符
    private static char[] ops = new char[] {'+', '-', '*'};

    /**
     * 生成验证码图片，并将结果保存到redis中
     */
    public static VerifyCodeVo createVerifyCode() {
        VerifyCodeVo verifyCodeVo = new VerifyCodeVo();
        // 第一步：定义宽度和高度
        int width = 80;
        int height = 32;
        // 第二步：create the image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 第三步：创建绘图
        Graphics g = image.getGraphics();
        // 第四步：设置绘图背景颜色和背景颜色的填充
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        // 第五步：设置画笔颜色，画了个矩形
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        // 第六步：创建随机数
        Random rdm = new Random();
        // 第七步：画50个点
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // 第八步：生成验证码
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
        // 第九步：计算验证码
        int codeResult = calc(verifyCode);
        verifyCodeVo.setBufferedImage(image);
        verifyCodeVo.setCodeResult(codeResult);
        return verifyCodeVo;
    }

    /**
     * 生成验证码，即生成一个数学表达式如：1-3*8
     * 只做+ - *
     */
    private static String generateVerifyCode(Random rdm) {
        //取10以内的随机数，包括0 不包括10
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        //取3以内的随机数，包括0 不包括3
        char op1 = ops[rdm.nextInt(3)];
        char op2 = ops[rdm.nextInt(3)];
        String exp = ""+ num1 + op1 + num2 + op2 + num3;
        return exp;
    }

    /**
     * 计算验证码
     * @param exp   一个数学表达式如：1-3*8
     * @return      返回结构
     */
    public static int calc(String exp) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer)engine.eval(exp);
        }catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
