package com.mvc.console.dto;

import lombok.Data;

import java.math.BigInteger;

/**
 * userDTO
 *
 * @author qiyichen
 * @create 2018/1/8 21:55
 */
@Data
public class PwdCheckDTO {

    private String password;
    private BigInteger userId;
}
