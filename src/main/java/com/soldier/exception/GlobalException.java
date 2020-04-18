package com.soldier.exception;

import com.soldier.result.CodeMsg;

/**
 * @Author soldier
 * @Date 20-4-18 下午6:24
 * @Email:583406411@qq.com
 * @Version 1.0
 * @Description:全局异常
 */
public class GlobalException extends RuntimeException {

    private static final long serialVersionUID = -8932732803559222280L;

    // 返回客户端的信息码
    private CodeMsg codeMsg;

    public GlobalException(CodeMsg codeMsg) {
        super(codeMsg.toString());
        this.codeMsg = codeMsg;
    }

    public CodeMsg getCodeMsg() {
        return codeMsg;
    }

    public void setCodeMsg(CodeMsg codeMsg) {
        this.codeMsg = codeMsg;
    }
}
