package com.mvc.console.entity;

import lombok.Data;

import javax.persistence.Table;
import java.math.BigInteger;
import java.util.Date;

/**
 * refund_application
 *
 * @author qiyichen
 * @create 2018/1/10 12:12
 */
@Data
@Table(name = "refund_application")
public class RefundApplication {

    private BigInteger id;
    private BigInteger userId;
    private String realName;
    private String idCardNo;
    private BigInteger refundQuantity;
    private String photoPath1;
    private String photoPath2;
    private String photoPath3;
    private String photoPath4;
    private String comment;
    private Integer status;
    private Date createdAt;
    private String cellphone;

}
