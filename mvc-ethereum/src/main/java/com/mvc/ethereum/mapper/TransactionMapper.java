package com.mvc.ethereum.mapper;

import com.alibaba.fastjson.JSONObject;
import com.mvc.common.dto.TransactionDTO;
import com.mvc.ethereum.model.Transaction;
import com.mvc.ethereum.model.vo.DepositeCount;
import com.mvc.ethereum.model.vo.TransactionVO;
import com.mvc.ethereum.model.vo.WithdrawCount;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

import java.math.BigInteger;
import java.util.List;

/**
 * @author qyc
 */
public interface TransactionMapper extends Mapper<Transaction>, InsertListMapper<Transaction> {
    /**
     * updateByOrderId
     *
     * @param orderId
     * @param hash
     * @param status
     */
    @Update("update transaction set status = #{status}, tx_hash = #{hash} where order_id = #{orderId}")
    void updateByOrderId(@Param("orderId") String orderId, @Param("hash") String hash, @Param("status") Integer status);

    /**
     * updateByHash
     *
     * @param hash
     */
    @Update("update transaction set status = 2 where tx_hash = #{hash}")
    void updateByHash(@Param("hash") String hash);

    /**
     * list
     *
     * @param transactionDTO
     * @return
     */
    @Select({"<script>",
            "SELECT * FROM transaction",
            "WHERE type = #{type}",
            "<when test=\"key!=null and key !=''\">",
            "AND (user_id = #{key} or tx_hash = #{key})",
            "</when>",
            "order by id desc",
            "</script>"})
    List<TransactionVO> list(TransactionDTO transactionDTO);

    /**
     * withdrawCount
     *
     * @param type
     * @param unit
     * @return
     */
    @Select("SELECT #{coinId} coin_id, IFNULL( SUM(actual_quantity),0) actual_quantity, IFNULL(SUM(quantity),0) quantity, count(1) num FROM `transaction` where created_at > ${unit} AND type in (1, 2) AND `status` = 2 AND coin_id = #{coinId}")
    WithdrawCount withdrawCount(@Param("coinId") BigInteger type, @Param("unit") String unit);

    /**
     * depositeCount
     *
     * @param type
     * @param unit
     * @return
     */
    @Select("SELECT #{coinId} coin_id, IFNULL( SUM(actual_quantity),0) actual_quantity, IFNULL(SUM(quantity),0) quantity, count(1) num FROM `transaction` where created_at > ${unit} AND type in (0) AND `status` = 1 AND coin_id = #{coinId}")
    DepositeCount depositeCount(@Param("coinId") BigInteger type, @Param("unit") String unit);

    /**
     * lockCount
     *
     * @param type
     * @param unit
     * @param status
     * @return
     */
    @Select("SELECT #{coinId} coin_id, IFNULL(sum(quantity),0) balance, IFNULL(sum(interest),0) interest, count(1) num FROM lock_record WHERE `status` in (${status}) AND created_at > ${unit} AND coin_id = #{coinId}")
    JSONObject lockCount(@Param("coinId") BigInteger type, @Param("unit") String unit, @Param("status") String status);

    @Select("SELECT * from transaction where type = 1 and status = 1 and id > #{id} limit 1")
    Transaction selectWaitTrans(BigInteger id);

    @Update("update transaction set status = #{status} where orderId = #{orderId}")
    void updateStatusByOrderId(@Param("orderId") String orderId, @Param("status")  Integer status);

    @Select("select * from transaction where to_address = #{addr} and type = 0 order by id desc limit 1")
    Transaction selectLast(String addr);
}