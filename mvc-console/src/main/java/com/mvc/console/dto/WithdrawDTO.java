package com.mvc.console.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * WithdrawDTO
 *
 * @author qiyichen
 * @create 2018/1/10 16:10
 */
@Data
public class WithdrawDTO {

    private BigInteger userId;
    private String address;
    private String password;
    private String valiCode;
    private BigDecimal value;
    private String type;
}
