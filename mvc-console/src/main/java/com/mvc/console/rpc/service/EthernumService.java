package com.mvc.console.rpc.service;

import com.alibaba.fastjson.JSONObject;
import com.mvc.api.vo.log.LogInfo;
import com.mvc.common.msg.Result;
import com.mvc.console.dto.SendTransactionDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("mvc-etherenum")
public interface EthernumService {

    @RequestMapping(value = "/{contractAddress}/eth_sendTransaction", method = RequestMethod.POST)
    Result approveAndCall(@PathVariable("contractAddress") String contractAddress, @RequestBody SendTransactionDTO sendTransactionDTO, @RequestHeader("Authorization") String authorization);

    @RequestMapping(value = "ethereum/wallet_account", method = RequestMethod.GET)
    Result<JSONObject> getAccount(@RequestParam("type") String type);

}
