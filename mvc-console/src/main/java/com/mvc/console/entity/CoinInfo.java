package com.mvc.console.entity;

import lombok.Data;

import javax.persistence.Id;
import java.math.BigInteger;
import java.util.Date;

/**
 * coin info
 *
 * @author qiyichen
 * @create 2018/1/9 16:28
 */
@Data
public class CoinInfo {
    @Id
    private BigInteger id;
    private String name;
    private String abbr;
    private Integer digit;
    private BigInteger ratio;
    private Integer status;
    private String icon;
    private String address;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
    private String nameEn;

}
