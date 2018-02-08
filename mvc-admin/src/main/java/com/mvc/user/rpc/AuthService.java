package com.mvc.user.rpc;

import com.alibaba.fastjson.JSONObject;
import com.mvc.user.entity.JwtAuthenticationRequest;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * @author qyc
 * @create 2017-06-21 8:11
 */
@FeignClient("mvc-auth")
public interface AuthService {
    /**
     * createAuthenticationToken
     *
     * @param authenticationRequest
     * @return
     */
    @RequestMapping(value = "jwt/token", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> createAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest);
}
