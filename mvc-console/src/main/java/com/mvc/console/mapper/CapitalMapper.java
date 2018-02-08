package com.mvc.console.mapper;

import com.mvc.console.entity.Capital;
import com.mvc.console.vo.CapitalVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigInteger;
import java.util.List;

/**
 * @author qyc
 */
public interface CapitalMapper extends Mapper<Capital> {
    /**
     * selectByUserId
     *
     * @param userId
     * @return
     */
    @Select("select * from capital where user_id = #{userId}")
    List<CapitalVO> selectByUserId(BigInteger userId);

    /**
     * updateValue
     *
     * @param quantity
     * @param userId
     * @param coinId
     * @param interest
     */
    @Update("update capital set locked = locked + #{quantity}, interest = interest + #{interest} where user_id = #{userId} and coin_id = #{coinId}")
    void updateValue(@Param("quantity") BigInteger quantity, @Param("userId") BigInteger userId, @Param("coinId") BigInteger coinId, @Param("interest") BigInteger interest);
}