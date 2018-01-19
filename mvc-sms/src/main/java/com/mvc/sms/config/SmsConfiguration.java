package com.mvc.sms.config;

import com.yunpian.sdk.YunpianClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ace on 2017/9/8.
 */
@Configuration()
public class SmsConfiguration{

    @Value("${yunpian.api.key}")
    private String apikey;

    @Bean
    public YunpianClient yunpianClient(){
        YunpianClient clnt = new YunpianClient(apikey).init();
        return clnt;
    }

}
