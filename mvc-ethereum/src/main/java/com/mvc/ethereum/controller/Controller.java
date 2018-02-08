package com.mvc.ethereum.controller;

import com.mvc.common.msg.Result;
import com.mvc.common.msg.ResultGenerator;
import com.mvc.ethereum.model.dto.BalanceDTO;
import com.mvc.ethereum.model.dto.SendTransactionDTO;
import com.mvc.ethereum.model.dto.TransactionCountDTO;
import com.mvc.ethereum.service.RpcService;
import com.mvc.ethereum.service.TransationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.web3j.protocol.core.methods.request.Transaction;

/**
 * @author qyc
 */
@Api("ERC-20 token standard API")
@RestController
public class Controller {
    @Autowired
    private com.mvc.ethereum.service.ContractService contractService;
    @Autowired
    private TransationService transationService;

    @Autowired
    private RpcService rpcService;

    @ApiOperation("Get token balance for address")
    @RequestMapping(
            value = "/{contractAddress}/eth_getBalance", method = RequestMethod.POST)
    public Object balanceOf(
            @PathVariable String contractAddress,
            @RequestBody final BalanceDTO balanceDTO) {
        return contractService.balanceOf(contractAddress, balanceDTO.getAddress());
    }

    @RequestMapping(value = "/{contractAddress}/eth_sendTransaction", method = RequestMethod.POST)
    public Result approveAndCall(@PathVariable String contractAddress, @RequestBody SendTransactionDTO sendTransactionDTO) throws Exception {
        Transaction transaction = transationService.buildTransaction(sendTransactionDTO.getTo(), sendTransactionDTO.getFrom(), sendTransactionDTO.getValue().toBigInteger());
        Object result = rpcService.eth_sendTransaction(transaction, sendTransactionDTO.getPass(), contractAddress);
        return ResultGenerator.genSuccessResult(result);
    }

    @RequestMapping(value = "/{contractAddress}/txList", method = RequestMethod.POST)
    private Object txList(@PathVariable String contractAddress, @RequestBody TransactionCountDTO transactionCountDTO) {
        return rpcService.txList(transactionCountDTO.getAddress());
    }

}
