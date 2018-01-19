package com.mvc.auth.service;


import java.math.BigInteger;

public interface AuthService {
    String login(String username, String password) throws Exception;
    String refresh(String oldToken);
    void validate(String token) throws Exception;
    Boolean invalid(String token);

    String loginUser(String username, String clientId, BigInteger userId, String address) throws Exception;
}
