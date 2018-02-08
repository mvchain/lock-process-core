package com.mvc.common.exception.auth;


import com.mvc.common.constant.CommonConstants;
import com.mvc.common.exception.BaseException;

/**
 * @author qyc
 */
public class ClientForbiddenException extends BaseException {
    public ClientForbiddenException(String message) {
        super(message, CommonConstants.EX_CLIENT_FORBIDDEN_CODE);
    }

}
