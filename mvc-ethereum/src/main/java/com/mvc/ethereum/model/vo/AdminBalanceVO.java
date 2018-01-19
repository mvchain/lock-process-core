package com.mvc.ethereum.model.vo;

import com.alibaba.fastjson.JSONObject;
import com.mvc.ethereum.utils.CoinUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

/**
 * AdminBalanceVO
 *
 * @author qiyichen
 * @create 2018/1/15 17:20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminBalanceVO {

    private BigInteger balance;
    private WithdrawCount withdraw;
    private DepositeCount deposite;
    private LockCount lock;
    private BigInteger coinId;

    public Double getBalanceStr() {
        return  CoinUtil.wei2Value(coinId, balance);
    }

}
