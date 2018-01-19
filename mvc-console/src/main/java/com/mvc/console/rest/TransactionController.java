package com.mvc.console.rest;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mvc.common.context.BaseContextHandler;
import com.mvc.common.msg.Result;
import com.mvc.common.msg.ResultGenerator;
import com.mvc.common.rest.BaseController;
import com.mvc.console.service.TransationService;
import com.mvc.console.dto.DepositeDTO;
import com.mvc.console.dto.TransactionDTO;
import com.mvc.console.dto.WithdrawDTO;
import com.mvc.console.entity.Transaction;
import com.mvc.console.vo.CapitalVO;
import com.mvc.console.vo.TransactionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author wanghaobin
 * @create 2017-06-08 11:51
 */
@RestController
@RequestMapping("transaction")
public class TransactionController extends BaseController<TransationService, Transaction> {

    @Autowired
    private TransationService transationService;


    // 查询我的资金
    @RequestMapping(value = "balance")
    public @ResponseBody
    Result<List<CapitalVO>> getBalance() {
        String userId = BaseContextHandler.getUserID();
        List<CapitalVO> result = transationService.getBalance(new BigInteger(userId));
        return ResultGenerator.genSuccessResult(result);
    }

    // 查询我的资金记录
    @RequestMapping(value = "list")
    public @ResponseBody
    Result<PageInfo> transactionList(TransactionDTO transactionDTO) {
        transactionDTO.setUserId(new BigInteger(BaseContextHandler.getUserID()));
        PageHelper.startPage(transactionDTO.getPageNo(), transactionDTO.getPageSize());
        List<TransactionVO> list = transationService.list(transactionDTO);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    // 提现
    @RequestMapping(value = "withdraw", method = RequestMethod.POST)
    public @ResponseBody
    Result withdraw(@RequestBody WithdrawDTO withdrawDTO, @RequestHeader("Authorization") String Authorization) {
        transationService.withdraw(withdrawDTO, Authorization);
        return ResultGenerator.genSuccessResult();
    }

    // 存入
    @RequestMapping(value = "deposite", method = RequestMethod.POST)
    public @ResponseBody
    Result deposite(@RequestBody DepositeDTO depositeDTO) {
        transationService.deposite(depositeDTO);
        return ResultGenerator.genSuccessResult();
    }

    // 查询当日已提现金额
    @RequestMapping(value = "balance/cost")
    public @ResponseBody
    Result<Double> surplus(@RequestParam("type") String type) {
        return ResultGenerator.genSuccessResult(transationService.getLimit(type));
    }

}
