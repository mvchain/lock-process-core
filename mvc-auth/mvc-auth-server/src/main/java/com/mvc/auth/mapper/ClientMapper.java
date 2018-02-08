package com.mvc.auth.mapper;

import com.mvc.auth.entity.Client;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author qyc
 */
public interface ClientMapper extends Mapper<Client> {
    /**
     * selectAllowedClient
     *
     * @param serviceId
     * @return
     */
    List<String> selectAllowedClient(String serviceId);

    /**
     * selectAuthorityServiceInfo
     *
     * @param clientId
     * @return
     */
    List<Client> selectAuthorityServiceInfo(int clientId);
}