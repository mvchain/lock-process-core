package com.mvc.auth.client.exception;

/**
 * @author qyc
 */
public class JwtTokenExpiredException extends Exception {
    public JwtTokenExpiredException(String s) {
        super(s);
    }
}
