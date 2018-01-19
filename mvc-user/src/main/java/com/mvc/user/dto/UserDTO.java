package com.mvc.user.dto;

import com.mvc.user.entity.User;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * userDTO
 *
 * @author qiyichen
 * @create 2018/1/8 21:55
 */
@Data
public class UserDTO extends User{

    @NotNull(message = "验证码不能为空")
    private String valiCode;
}
