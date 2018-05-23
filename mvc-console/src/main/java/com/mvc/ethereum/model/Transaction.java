package com.mvc.ethereum.model;

import lombok.Data;

import javax.persistence.Id;
import java.math.BigInteger;
import java.util.Date;

/**
 * @author qiyichen
 * @create 2018/5/23 17:23
 */
@Data
public class Transaction {
    @Id
    private BigInteger id;
    private BigInteger userId;
    private Integer type;
    private Integer status;
    private String txHash;
    private BigInteger quantity;
    private BigInteger actualQuantity;
    private Date createdAt;
    private Date updatedAt;
    private BigInteger coinId;
    private String orderId;
    private String toAddress;
    private String fromAddress;
}