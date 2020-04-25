package com.soldier.vo;

import java.awt.image.BufferedImage;

/**
 * @Author soldier
 * @Date 20-4-25 下午1:37
 * @Email:583406411@qq.com
 * @Version 1.0
 * @Description:保存验证码信息
 */
public class VerifyCodeVo {

    // 生成的图片流
    private BufferedImage bufferedImage;
    // 验证的结果
    private int codeResult;

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    public int getCodeResult() {
        return codeResult;
    }

    public void setCodeResult(int codeResult) {
        this.codeResult = codeResult;
    }
}
