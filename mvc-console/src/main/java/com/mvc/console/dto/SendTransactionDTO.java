package com.mvc.console.dto;

import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIInlineBinaryData;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

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