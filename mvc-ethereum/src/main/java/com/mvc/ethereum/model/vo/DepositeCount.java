package com.mvc.ethereum.model.vo;

import com.mvc.ethereum.utils.CoinUtil;
import lombok.Data;

import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/1/16 11:27
 */
@Data
public class DepositeCount {

    private BigInteger actualQuantity;
    private BigInteger quantity;
    private Integer num;
    private BigInteger coinId;
    private Double actualQuantityStr;
    private Double quantityStr;

    public Double getActualQuantityStr() {
        return  CoinUtil.wei2Value(coinId, actualQuantity);
    }

    public Double getQuantityStr() {
        return  CoinUtil.wei2Value(coinId, quantity);
    }

}
