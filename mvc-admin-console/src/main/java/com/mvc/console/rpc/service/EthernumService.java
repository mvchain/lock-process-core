package com.mvc.console.rpc.service;

import com.mvc.api.vo.log.LogInfo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("mvc-etherenum")
public interface EthernumService {

    @RequestMapping(value="/api/log/save",method = RequestMethod.POST)
    public void saveLog(LogInfo info);

}
