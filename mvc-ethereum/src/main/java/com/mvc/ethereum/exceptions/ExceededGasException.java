package com.mvc.ethereum.exceptions;

/**
 * @author qyc
 */
public class ExceededGasException extends Throwable {
    public ExceededGasException(String message) {
        super(message);
    }
}
