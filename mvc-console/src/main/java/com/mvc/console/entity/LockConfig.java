package com.mvc.console.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * LockConfig
 *
 * @author qiyichen
 * @create 2018/1/13 6:30
 */
@Data
public class LockConfig {

    private Integer switchKey;
    private String type;
    private BigDecimal min;
    private BigDecimal max;
    private Integer month;
    // 利率
    private Float interest;
}
