package com.mvc.console.entity;

import lombok.Data;

import javax.persistence.Id;
import java.math.BigInteger;
import java.util.Date;

/**
 * lock_record
 *
 * @author qiyichen
 * @create 2018/1/9 16:33
 */
@Data
public class LockRecord {
    @Id
    private BigInteger id;
    private BigInteger userId;
    private BigInteger quantity;
    private Integer status;
    private Date createdAt;
    private String orderId;
    private BigInteger coinId;
    private BigInteger interest;
    private Float interestRate;
    private Integer month;
    private Date updatedAt;

}
