package com.mvc.auth.util.user;

import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * @author qyc
 */
@Data
public class JwtUserAuthenticationRequest implements Serializable {

    private static final long serialVersionUID = -8445943548965154778L;

    private String userName;
    private String clientId;
    private BigInteger userId;
    private String address;
}
