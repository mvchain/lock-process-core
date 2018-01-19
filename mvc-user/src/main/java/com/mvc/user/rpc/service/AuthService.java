package com.mvc.user.rpc.service;

import com.mvc.common.msg.Result;
import com.mvc.user.dto.TokenDTO;
import com.mvc.user.vo.JwtAuthenticationResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * sms remote rpc
 *
 * @author qiyichen
 * @create 2018/1/8 21:48
 */
@FeignClient("mvc-auth")
public interface AuthService {

    @RequestMapping(value="jwt/user/token",method = RequestMethod.POST)
    public Result<String> createUserAuthenticationToken(@RequestBody TokenDTO tokenDTO);

    @RequestMapping(value = "client/validate", method = RequestMethod.GET)
    public Result valicode(@RequestParam("valiCode") String valiCode) throws Exception;
}
