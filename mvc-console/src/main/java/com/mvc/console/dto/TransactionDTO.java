package com.mvc.console.dto;

import lombok.Data;

import java.math.BigInteger;

/**
 * transaction dto
 *
 * @author qiyichen
 * @create 2018/1/10 12:51
 */
@Data
public class TransactionDTO {

    private Integer pageNo;
    private Integer pageSize;
    //0充值 1提现 2手动提现
    private String types;
    private BigInteger userId;
}
