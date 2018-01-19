package com.mvc.common.msg;

/**
 * 响应码枚举，参考HTTP状态码的语义
 * @author 张洁
 * @date 2017/11/02
 */
public enum ResultCode {
    /**成功*/
    SUCCESS(200),
    /**失败*/
    FAILURE(400),
    /**未认证*/
    UNAUTHORIZED(401),
    /**接口不存在*/
    NOT_FOUND(404),
    /**服务器内部错误*/
    INTERNAL_SERVER_ERROR(500),
    /**请求参数校验不通过*/
    VALIDATE_ERROR(600),
    /**业务异常，如*/
    BUSINESS_ERROR(1000);

    public int code;

    ResultCode(int code) {
        this.code = code;
    }
}
