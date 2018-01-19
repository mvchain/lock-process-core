package com.mvc.user.vo;

import com.mvc.user.entity.User;
import lombok.Data;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * UserVO
 *
 * @author qiyichen
 * @create 2018/1/12 16:26
 */
@Data
public class UserVO {

    private BigInteger id;

    private String cellphone;

    private String addressEth;

    private Integer status;

    private Date createdAt;

    private Date updatedAt;

    private Date deletedAt;

    private List<CapitalVO> list;
}
