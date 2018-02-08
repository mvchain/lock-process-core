package com.mvc.common.exception.auth;


import com.mvc.common.constant.CommonConstants;
import com.mvc.common.exception.BaseException;

/**
 * @author qyc
 */
public class TokenErrorException extends BaseException {
    public TokenErrorException(String message, int status) {
        super(message, CommonConstants.EX_TOKEN_ERROR_CODE);
    }
}
