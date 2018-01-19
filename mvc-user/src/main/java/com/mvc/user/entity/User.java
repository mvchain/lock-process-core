package com.mvc.user.entity;

import lombok.Data;

import java.math.BigInteger;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Table(name = "user")
@Data
public class User {
    @Id
    private BigInteger id;

    @NotNull(message = "请输入密码")
    private String password;

    @Pattern(regexp = "^(0\\d{2}-\\d{8}(-\\d{1,4})?)|(0\\d{3}-\\d{7,8}(-\\d{1,4})?)|" +
            "(1(([3]\\d{9})|(4[57]\\d{8})|(5[0-3|5-9]\\d{8})|(7[0-3|5-8]\\d{8})|(8\\d{9})))$" , message = "手机号格式不正确！")
    private String cellphone;

    private String addressEth;

    private Integer status;

    private Date createdAt;

    private Date updatedAt;

    private Date deletedAt;

    public void init() {
        this.status = 1;
    }
}