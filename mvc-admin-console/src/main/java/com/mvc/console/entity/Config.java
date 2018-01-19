package com.mvc.console.entity;

import lombok.Data;

import java.math.BigInteger;
import java.util.Date;

/**
 * config
 *
 * @author qiyichen
 * @create 2018/1/10 14:05
 */
@Data
public class Config {

    private BigInteger id;
    private String config;
    private Integer type;
    private Integer switchKey;
    private Integer status;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;

}
