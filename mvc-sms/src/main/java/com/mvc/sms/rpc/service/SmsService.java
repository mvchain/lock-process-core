package com.mvc.sms.rpc.service;

import com.mvc.common.constant.CommonConstants;
import com.yunpian.sdk.YunpianClient;
import com.yunpian.sdk.model.Result;
import com.yunpian.sdk.model.SmsSingleSend;
import io.jsonwebtoken.lang.Assert;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by ace on 2017/9/12.
 */
@Service
public class SmsService {

    @Autowired
    private YunpianClient yunpianClient;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private final static Long EXPIRE = 5L;

    public void getSmsValiCode(String mobile){
        String key = CommonConstants.SMS_VALI_PRE + mobile;
        int code = (int) ((Math.random() * 9 + 1) * 100000);
        //发送短信API
        Map<String, String> param = yunpianClient .newParam(2);
        param.put(YunpianClient.MOBILE, mobile);
        param.put(YunpianClient.TEXT, String.format("【牛视科技】您的验证码是%s", code));
        Result<SmsSingleSend> r = yunpianClient.sms().single_send(param);
        Assert.isTrue( r.isSucc(), StringUtils.isBlank(r.getMsg())?r.getDetail():r.getMsg());
        redisTemplate.opsForValue().set(key, code, EXPIRE, TimeUnit.MINUTES);
    }

    public Boolean checkSmsValiCode(String mobile, String code) {
        String key = CommonConstants.SMS_VALI_PRE + mobile;
        String valiCode = "" + redisTemplate.opsForValue().get(key);
        if (ObjectUtils.equals(valiCode, code)) {
            redisTemplate.delete(key);
            return true;
        }
        return false;
    }
}
