package com.mvc.common.msg.auth;

import com.mvc.common.constant.RestCodeConstants;
import com.mvc.common.msg.BaseResponse;

/**
 * @author qyc
 */
public class TokenErrorResponse extends BaseResponse {
    public TokenErrorResponse(String message) {
        super(RestCodeConstants.TOKEN_ERROR_CODE, message);
    }
}
