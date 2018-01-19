package com.mvc.ethereum.model.vo;

import com.mvc.ethereum.utils.CoinUtil;
import lombok.Data;

import java.math.BigInteger;

/**
 * LockCount
 *
 * @author qiyichen
 * @create 2018/1/16 11:27
 */
@Data
public class LockCount {

    private BigInteger allBalance;
    private BigInteger nowBalance;
    private BigInteger unlockBalance;
    private BigInteger allInterest;
    private BigInteger nowInterest;
    private BigInteger unlockInterest;
    private Integer allNum;
    private Integer nowNum;
    private Integer unlockNum;
    private BigInteger coinId;


    public Double getAllBalanceStr() {
        return  CoinUtil.wei2Value(coinId, allBalance);
    }

    public Double getNowBalanceStr() {
        return  CoinUtil.wei2Value(coinId, nowBalance);
    }

    public Double getUnlockBalanceStr() {
        return  CoinUtil.wei2Value(coinId, unlockBalance);
    }

    public Double getAllInterestStr() {
        return  CoinUtil.wei2Value(coinId, allInterest);
    }

    public Double getNowInterestStr() {
        return  CoinUtil.wei2Value(coinId, nowInterest);
    }

    public Double getUnlockInterestStr() {
        return  CoinUtil.wei2Value(coinId, unlockInterest);
    }

}
