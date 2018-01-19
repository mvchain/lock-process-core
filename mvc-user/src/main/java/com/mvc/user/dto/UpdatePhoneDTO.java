package com.mvc.user.dto;

import com.mvc.user.entity.User;
import lombok.Data;

/**
 * update phone dto
 *
 * @author qiyichen
 * @create 2018/1/9 14:10
 */
@Data
public class UpdatePhoneDTO extends UserDTO {

    private String newCellPhone;
}
