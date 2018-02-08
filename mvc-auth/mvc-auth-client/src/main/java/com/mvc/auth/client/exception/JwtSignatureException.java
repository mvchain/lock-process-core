package com.mvc.auth.client.exception;

/**
 * @author qyc
 */
public class JwtSignatureException extends Exception {
    public JwtSignatureException(String s) {
        super(s);
    }
}
