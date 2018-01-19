package com.mvc.console.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigInteger;
import java.util.Date;

/**
 * capital
 *
 * @author qiyichen
 * @create 2018/1/9 16:19
 */
@Data
@Table(name = "capital")
public class Capital {

    @Id
    private BigInteger id;
    private BigInteger userId;
    private BigInteger balance;
    private BigInteger locked;
    private Integer status;
    private Date createdAt;
    private Date updatedAt;
    private BigInteger coinId;
    private  BigInteger interest;

}
