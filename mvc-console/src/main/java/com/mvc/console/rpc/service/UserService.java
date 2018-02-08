package com.mvc.console.rpc.service;

import com.mvc.common.msg.Result;
import com.mvc.console.dto.PwdCheckDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * sms remote rpc
 *
 * @author qiyichen
 * @create 2018/1/8 21:48
 */
@FeignClient("mvc-user")
public interface UserService {
    /**
     * checkPwd
     *
     * @param pwdCheckDTO
     * @param authorization
     * @return
     */
    @RequestMapping(value = "checkPwd", method = RequestMethod.POST)
    Result checkPwd(@RequestBody PwdCheckDTO pwdCheckDTO, @RequestHeader("Authorization") String authorization);

    /**
     * getEthnumKey
     *
     * @param authorization
     * @return
     */
    @RequestMapping(value = "ethnumKey", method = RequestMethod.GET)
    Result<String> getEthnumKey(@RequestHeader("Authorization") String authorization);

}
