package com.mvc.user.dto;

import com.mvc.user.entity.User;
import lombok.Data;

import javax.validation.constraints.NotNull;
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
