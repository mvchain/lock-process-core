package com.mvc.auth.mapper;

import com.mvc.auth.entity.ClientService;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author qyc
 */
public interface ClientServiceMapper extends Mapper<ClientService> {

    /**
     * deleteByServiceId
     *
     * @param id
     */
    void deleteByServiceId(int id);
}