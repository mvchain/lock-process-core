package com.mvc.auth.common.util.jwt;

/**
 * Created by ace on 2017/9/10.
 */
public interface IJwtInfo {
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
