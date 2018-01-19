package com.mvc.user.dto;

import lombok.Data;

/**
 * userDTO
 *
 * @author qiyichen
 * @create 2018/1/8 21:55
 */
@Data
public class ForgetDTO extends UserDTO{

    private String newPassword;
}
