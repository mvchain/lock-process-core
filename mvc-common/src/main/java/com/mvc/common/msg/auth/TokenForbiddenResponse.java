package com.mvc.common.msg.auth;

import com.mvc.common.constant.RestCodeConstants;
import com.mvc.common.msg.BaseResponse;

/**
 * @author qyc
 */
public class TokenForbiddenResponse extends BaseResponse {
    public TokenForbiddenResponse(String message) {
        super(RestCodeConstants.TOKEN_FORBIDDEN_CODE, message);
    }
}
