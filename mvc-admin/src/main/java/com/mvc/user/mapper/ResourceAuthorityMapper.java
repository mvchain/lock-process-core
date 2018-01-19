package com.mvc.user.mapper;

import com.mvc.user.entity.ResourceAuthority;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author qyc
 */
public interface ResourceAuthorityMapper extends Mapper<ResourceAuthority> {
    /**
     * deleteByAuthorityIdAndResourceType
     *
     * @param authorityId
     * @param resourceType
     */
    void deleteByAuthorityIdAndResourceType(@Param("authorityId") String authorityId, @Param("resourceType") String resourceType);
}