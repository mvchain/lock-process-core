package com.mvc.auth.client.exception;

/**
 * Created by ace on 2017/9/15.
 */
public class JwtSignatureException extends Exception {
    public JwtSignatureException(String s) {
        super(s);
    }
}
