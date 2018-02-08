package com.mvc.common.exception.auth;


import com.mvc.common.constant.CommonConstants;
import com.mvc.common.exception.BaseException;

/**
 * @author qyc
 */
public class ClientInvalidException extends BaseException {
    public ClientInvalidException(String message) {
        super(message, CommonConstants.EX_CLIENT_INVALID_CODE);
    }
}
