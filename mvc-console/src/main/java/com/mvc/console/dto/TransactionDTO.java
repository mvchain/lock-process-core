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
    private String types;
    private BigInteger userId;
}
