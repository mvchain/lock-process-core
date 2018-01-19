package com.mvc.user.mapper;

import com.mvc.user.entity.Menu;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author qyc
 */
public interface MenuMapper extends Mapper<Menu> {
    /**
     * selectMenuByAuthorityId
     *
     * @param authorityId
     * @param authorityType
     * @return
     */
    List<Menu> selectMenuByAuthorityId(@Param("authorityId") String authorityId, @Param("authorityType") String authorityType);

    /**
     * selectAuthorityMenuByUserId
     *
     * @param userId
     * @return
     */
    List<Menu> selectAuthorityMenuByUserId(@Param("userId") int userId);

    /**
     * selectAuthoritySystemByUserId
     *
     * @param userId
     * @return
     */
    List<Menu> selectAuthoritySystemByUserId(@Param("userId") int userId);
}