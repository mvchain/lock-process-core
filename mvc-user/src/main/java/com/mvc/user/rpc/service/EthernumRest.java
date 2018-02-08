package com.mvc.user.rpc.service;

import com.mvc.api.vo.user.NewAccountDTO;
import com.mvc.common.msg.Result;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * ${DESCRIPTION}
 *
 * @author wanghaobin
 * @create 2017-06-21 8:15
 */
@FeignClient("mvc-etherenum")
public interface EthernumRest {

    /**
     * personal_newAccount
     *
     * @param newAccountDTO
     * @return
     */
    @RequestMapping(value = "ethereum/personal_newAccount", method = RequestMethod.POST)
    Result<String> personal_newAccount(@RequestBody NewAccountDTO newAccountDTO);

}
