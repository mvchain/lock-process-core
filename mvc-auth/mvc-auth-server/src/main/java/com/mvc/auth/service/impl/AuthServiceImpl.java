package com.mvc.auth.service.impl;

import com.mvc.api.vo.user.UserInfo;
import com.mvc.auth.common.util.jwt.JWTInfo;
import com.mvc.auth.feign.IUserService;
import com.mvc.auth.service.AuthService;
import com.mvc.auth.util.user.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

/**
 * @author qyc
 */
@Service
public class AuthServiceImpl implements AuthService {

    private JwtTokenUtil jwtTokenUtil;
    private IUserService userService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Value("${jwt.expire}")
    private int expire;

    @Autowired
    public AuthServiceImpl(
            JwtTokenUtil jwtTokenUtil,
            IUserService userService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @Override
    public String login(String username, String password) throws Exception {
        UserInfo info = userService.validate(username, password);
        String token = "";
        if (!StringUtils.isEmpty(info.getId())) {
            token = jwtTokenUtil.generateToken(new JWTInfo(info.getUsername(), info.getId() + "", "admin"));
        }
        String key = "USER_ADMIN_" + username;
        redisTemplate.opsForValue().set(key, "true");
        redisTemplate.expire(key, Long.valueOf(expire), TimeUnit.SECONDS);
        return token;
    }

    @Override
    public String loginUser(String username, String clientId, BigInteger userId, String address) throws Exception {
        String token = jwtTokenUtil.generateToken(new JWTInfo(username, userId.toString(), clientId, address));
        String key = "USER_" + clientId + "_" + username;
        redisTemplate.opsForValue().set(key, "true");
        redisTemplate.expire(key, Long.valueOf(expire), TimeUnit.SECONDS);
        return token;
    }

    @Override
    public void validate(String token) throws Exception {
        jwtTokenUtil.getInfoFromToken(token);
    }

    @Override
    public Boolean invalid(String token) {
        // TODO: 2017/9/11 注销token
        return null;
    }

    @Override
    public String refresh(String oldToken) {
        // TODO: 2017/9/11 刷新token
        return null;
    }
}
