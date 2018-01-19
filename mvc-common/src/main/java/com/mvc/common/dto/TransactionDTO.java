package com.mvc.common.dto;

import lombok.Data;

/**
 * TransactionDTO
 *
 * @author qiyichen
 * @create 2018/1/15 14:23
 */
@Data
public class TransactionDTO {

    private Integer pageSize;
    private Integer pageNo;
    private Integer type;
    private String key;

}
