package com.mvc.ethereum.mapper;


import com.mvc.ethereum.model.Capital;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigInteger;

/**
 * @author qyc
 */
public interface CapitalMapper extends Mapper<Capital> {
    /**
     * updateBalance
     *
     * @param coinId
     * @param userId
     * @param value
     * @return
     */
    @Update("update capital set balance = ( balance + ${value} ) where coin_id = #{coinId} and user_id = #{userId}")
    Integer updateBalance(@Param("coinId") BigInteger coinId, @Param("userId") BigInteger userId, @Param("value") String value);

    /**
     * updateLockBalance
     *
     * @param coinId
     * @param userId
     * @param value
     * @param locked
     * @return
     */
    @Update("update capital set balance = ( balance + ${value} ), locked = ( locked - ${locked}) where coin_id = #{coinId} and user_id = #{userId}")
    Integer updateLockBalance(@Param("coinId") BigInteger coinId, @Param("userId") BigInteger userId, @Param("value") String value, @Param("locked") String locked);
}