package com.mvc.ethereum.mapper;


import com.mvc.ethereum.model.Capital;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigInteger;

public interface CapitalMapper extends Mapper<Capital> {

    @Update("update capital set balance = ( balance + ${value} ) where coin_id = #{coinId} and user_id = #{userId}")
    Integer updateBalance(@Param("coinId") BigInteger coinId, @Param("userId") BigInteger userId, @Param("value") String value);
}