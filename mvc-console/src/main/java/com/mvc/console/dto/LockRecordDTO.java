package com.mvc.console.dto;

import com.mvc.console.entity.LockRecord;
import lombok.Data;

import java.math.BigDecimal;

/**
 * LockRecord
 *
 * @author qiyichen
 * @create 2018/1/11 19:44
 */
@Data
public class LockRecordDTO extends LockRecord{

    private String type;
    private BigDecimal value;
    private Integer pageNo;
    private Integer pageSize;

}
