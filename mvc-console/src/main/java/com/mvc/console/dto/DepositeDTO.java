package com.mvc.console.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * deposite dto
 *
 * @author qiyichen
 * @create 2018/1/10 17:06
 */
@Data
public class DepositeDTO {

    private String type;
    private BigDecimal value;
    private String password;
}
