package com.mvc.console.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

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
    // 手续费
    private Float poundage;

}
