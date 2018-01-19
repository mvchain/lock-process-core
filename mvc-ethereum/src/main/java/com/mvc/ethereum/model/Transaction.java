package com.mvc.ethereum.model;

import lombok.Data;

import javax.persistence.Id;
import java.math.BigInteger;
import java.util.Date;

/**
 * transaction
 *
 * @author qiyichen
 * @create 2018/1/9 16:35
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
