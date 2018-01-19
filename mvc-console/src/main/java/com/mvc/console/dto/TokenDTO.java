package com.mvc.console.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

/**
 * token dto
 *
 * @author qiyichen
 * @create 2018/1/9 10:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenDTO {
    private String userName;
    private String clientId;
    private BigInteger userId;
}
