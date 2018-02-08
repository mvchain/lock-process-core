package com.mvc.auth.common.util.jwt;

/**
 * @author qyc
 */
public interface IJWTInfo {
    /**
     * 获取用户名
     * @return
     */
    String getUniqueName();

    /**
     * 获取用户ID
     * @return
     */
    String getId();

    /**
     * 获取名称
     * @return
     */
    String getName();

    /**
     * 获取地址
     * @return
     */
    String getAddress();
}
