package com.mvc.sms.rest;

import com.mvc.common.annotation.IgnoreUserToken;
import com.mvc.common.msg.BaseResponse;
import com.mvc.common.msg.Result;
import com.mvc.common.msg.ResultGenerator;
import com.mvc.sms.dto.MobileDTO;
import com.mvc.sms.dto.MobileValiDTO;
import com.mvc.sms.rpc.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * ${DESCRIPTION}
 *
 * @author wanghaobin
 * @create 2017-06-08 11:51
 */
@RestController
@RequestMapping("sms")
@ComponentScan
public class SmsController {

    @Autowired
    private SmsService smsService;

    @RequestMapping(value = "code", method = RequestMethod.POST)
    @ResponseBody
    public Result getSmsValiCode(@RequestBody MobileDTO mobileDTO) {
        smsService.getSmsValiCode(mobileDTO.getMobile());
        return ResultGenerator.genSuccessResult();
    }

    @IgnoreUserToken
    @RequestMapping(value = "code/validate", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<Boolean> getMenusByUsername(@RequestBody MobileValiDTO mobileValiDTO) throws Exception {
        Boolean result = smsService.checkSmsValiCode(mobileValiDTO.getMobile(), mobileValiDTO.getValiCode());
        return  ResponseEntity.ok(result);
    }

}
