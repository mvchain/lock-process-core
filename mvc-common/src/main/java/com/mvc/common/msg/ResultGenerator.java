package com.mvc.common.msg;

/**
 * 响应结果生成工具
 *
 * @author 张洁
 * @date 2017/11/02
 */
@SuppressWarnings("unused")
public class ResultGenerator {
    private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";

    public static Result genSuccessResult() {
        return new Result()
                .setCode(ResultCode.SUCCESS)
                .setMessage(DEFAULT_SUCCESS_MESSAGE);
    }

    public static <T> Result<T> genSuccessResult(T data) {
        return new Result<T>()
                .setCode(ResultCode.SUCCESS)
                .setMessage(DEFAULT_SUCCESS_MESSAGE)
                .setData(data);
    }

    public static Result genFailResult(String message) {
        return new Result()
                .setCode(ResultCode.FAILURE)
                .setMessage(message);
    }

    public static Result genFailResult(String message, Object data) {
        return new Result()
                .setCode(ResultCode.FAILURE)
                .setMessage(message)
                .setData(data);
    }

    public static Result genFailResult(Integer code, String message) {
        return new Result()
                .setCode(code)
                .setMessage(message);
    }
}
