package com.mvc.common.dto;

import lombok.Data;

/**
 * TransactionDTO
 *
 * @author qiyichen
 * @create 2018/1/15 14:23
 */
@Data
public class LockRecordDTO {

    private Integer pageSize;
    private Integer pageNo;
    private String key;

}
