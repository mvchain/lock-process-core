package com.mvc.auth.service;


import java.util.List;

/**
 * @author qyc
 */
public interface AuthClientService {
    /**
     * apply
     *
     * @param clientId
     * @param secret
     * @return
     * @throws Exception
     */
    public String apply(String clientId, String secret) throws Exception;

    /**
     * 获取授权的客户端列表
     *
     * @param serviceId
     * @param secret
     * @return
     */
    public List<String> getAllowedClient(String serviceId, String secret);

    /**
     * 获取服务授权的客户端列表
     *
     * @param serviceId
     * @return
     */
    public List<String> getAllowedClient(String serviceId);

    /**
     * validate
     *
     * @param clientId
     * @param secret
     * @throws Exception
     */
    public void validate(String clientId, String secret) throws Exception;
}
