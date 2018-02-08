package com.mvc.console.dto;

import lombok.Data;

import java.math.BigInteger;

/**
 * @author qyc
 */
@Data
public class SendTransactionDTO {

    private String pass;
    private String from;
    private String to;
    private BigInteger gas;
    private BigInteger gasPrice;
    private BigInteger value;
    private String data;
    private BigInteger nonce;

}