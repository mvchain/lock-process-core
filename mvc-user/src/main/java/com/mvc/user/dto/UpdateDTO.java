package com.mvc.user.dto;

import com.mvc.user.entity.User;
import lombok.Data;

/**
 * userDTO
 *
 * @author qiyichen
 * @create 2018/1/8 21:55
 */
@Data
public class UpdateDTO extends User {

    private String newPassword;
}
