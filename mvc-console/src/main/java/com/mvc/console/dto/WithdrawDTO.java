package com.mvc.console.dto;

import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
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
    @Length(min = 42, max = 42, message = "地址格式错误")
    @NonNull
    private String address;
    private String password;
    private String valiCode;
    @NonNull
    private BigDecimal value;
    private String type;
}
