package com.mvc.common.msg;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 统一API响应结果封装
 *
 * @author 张洁
 * @date 2017/11/02
 */
@SuppressWarnings("unused")
public class Result<T> {
    @JsonProperty("status")
    private int code;
    private String message;
    private T data;

    public Result<T> setCode(ResultCode resultCode) {
        this.code = resultCode.code;
        return this;
    }

    public int getCode() {
        return code;
    }

    public Result<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Result<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getData() {
        return data;
    }

    public Result<T> setData(T data) {
        this.data = data;
        return this;
    }
}
