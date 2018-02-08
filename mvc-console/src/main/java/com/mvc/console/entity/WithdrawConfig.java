package com.mvc.console.entity;

import com.mvc.console.util.CoinUtil;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * withdraw
 *
 * @author qiyichen
 * @create 2018/1/13 6:34
 */
@Data
public class WithdrawConfig {

    private Integer switchKey;
    private String type;
    private BigDecimal min;
    private BigDecimal max;
    private Float poundage;

    public BigInteger getPoundageValue(BigDecimal value, String type) {
        BigDecimal ratioInt = new BigDecimal(poundage).setScale(5, RoundingMode.HALF_DOWN);
        value.setScale(5, RoundingMode.HALF_DOWN);
        BigInteger result = CoinUtil.Value2wei(value.multiply(ratioInt), type);
        return result;
    }
}
