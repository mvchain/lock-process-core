package com.mvc.auth.service;


import java.math.BigInteger;

/**
 * @author qyc
 */
public interface AuthService {
    /**
     * login
     *
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    String login(String username, String password) throws Exception;

    /**
     * refresh
     *
     * @param oldToken
     * @return
     */
    String refresh(String oldToken);

    /**
     * validate
     *
     * @param token
     * @throws Exception
     */
    void validate(String token) throws Exception;

    /**
     * invalid
     *
     * @param token
     * @return
     */
    Boolean invalid(String token);

    /**
     * loginUser
     *
     * @param username
     * @param clientId
     * @param userId
     * @param address
     * @return
     * @throws Exception
     */
    String loginUser(String username, String clientId, BigInteger userId, String address) throws Exception;
}
