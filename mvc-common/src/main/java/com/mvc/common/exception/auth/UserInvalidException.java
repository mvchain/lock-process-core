package com.mvc.common.exception.auth;


import com.mvc.common.constant.CommonConstants;
import com.mvc.common.exception.BaseException;

/**
 * @author qyc
 */
public class UserInvalidException extends BaseException {
    public UserInvalidException(String message) {
        super(message, CommonConstants.EX_USER_INVALID_CODE);
    }
}
