package com.mvc.console.mapper;

import com.mvc.console.dto.TransactionDTO;
import com.mvc.console.entity.Transaction;
import com.mvc.console.vo.TransactionVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigInteger;
import java.util.List;

/**
 * @author qyc
 */
public interface TransactionMapper extends Mapper<Transaction> {
    /**
     * list
     *
     * @param transactionDTO
     * @return
     */
    @Select("select * from transaction where type in (${types}) and user_id = #{userId} order by created_at desc")
    List<TransactionVO> list(TransactionDTO transactionDTO);

    /**
     * costByDay
     *
     * @param userId
     * @param coinId
     * @param date
     * @return
     */
    @Select("SELECT SUM(quantity) FROM `transaction` WHERE user_id = #{userId} AND type = 1 AND coin_id = #{coinId} AND CAST(created_at AS DATE)=DATE #{data}")
    BigInteger costByDay(@Param("userId") BigInteger userId, @Param("coinId") BigInteger coinId, @Param("data") String date);
}