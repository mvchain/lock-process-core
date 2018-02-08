package com.mvc.user.rpc.service;

import com.mvc.user.dto.MobileValiDTO;
import com.mvc.api.vo.log.LogInfo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * sms remote rpc
 *
 * @author qiyichen
 * @create 2018/1/8 21:48
 */
@FeignClient("mvc-sms")
public interface SmsService {
    /**
     * checkSms
     * @param mobileValiDTO
     * @return
     */
    @RequestMapping(value="/sms/code/validate",method = RequestMethod.POST)
    ResponseEntity<Boolean> checkSms(@RequestBody MobileValiDTO mobileValiDTO);

}
