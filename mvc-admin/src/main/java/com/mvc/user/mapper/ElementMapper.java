package com.mvc.user.mapper;

import com.mvc.user.entity.Element;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author qyc
 */
public interface ElementMapper extends Mapper<Element> {
    /**
     * selectAuthorityElementByUserId
     *
     * @param userId
     * @return
     */
    List<Element> selectAuthorityElementByUserId(@Param("userId") String userId);

    /**
     * selectAuthorityMenuElementByUserId
     *
     * @param userId
     * @param menuId
     * @return
     */
    List<Element> selectAuthorityMenuElementByUserId(@Param("userId") String userId, @Param("menuId") String menuId);

    /**
     * selectAuthorityElementByClientId
     *
     * @param clientId
     * @return
     */
    List<Element> selectAuthorityElementByClientId(@Param("clientId") String clientId);

}