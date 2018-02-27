package com.mvc.console.service;

import com.alibaba.fastjson.JSONObject;
import com.mvc.common.biz.BaseBiz;
import com.mvc.common.context.BaseContextHandler;
import com.mvc.common.msg.Result;
import com.mvc.common.msg.ResultCode;
import com.mvc.console.dto.*;
import com.mvc.console.entity.Capital;
import com.mvc.console.entity.CoinInfo;
import com.mvc.console.entity.Transaction;
import com.mvc.console.mapper.TransactionMapper;
import com.mvc.console.rpc.service.EthernumService;
import com.mvc.console.rpc.service.SmsService;
import com.mvc.console.rpc.service.UserService;
import com.mvc.console.util.CoinUtil;
import com.mvc.console.util.ConfigUtil;
import com.mvc.console.vo.CapitalVO;
import com.mvc.console.vo.TransactionVO;
import io.jsonwebtoken.lang.Assert;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author wanghaobin
 * @create 2017-06-08 16:23
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TransationService extends BaseBiz<TransactionMapper, Transaction> {

    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private CapitalService capitalService;
    @Autowired
    private SmsService smsService;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private EthernumService ethernumService;

    private static HashMap<String, CoinInfo> coinMap;

    public List<CapitalVO> getBalance(BigInteger userId) {
        Capital capital = new Capital();
        capital.setUserId(userId);
        List<CapitalVO> result = capitalService.selectByUserId(capital);
        return result;
    }

    public List<TransactionVO> list(TransactionDTO transactionDTO) {
        return transactionMapper.list(transactionDTO);

    }

    public void withdraw(WithdrawDTO withdrawDTO, String authorization) throws UnsupportedEncodingException {
        // 校验余额
        checkWithdraw(withdrawDTO);
        // 校验验证码
        checkSms(withdrawDTO);
        // 校验密码
        checkPwd(withdrawDTO, authorization);
        Result<JSONObject> account = ethernumService.getAccount(withdrawDTO.getType());
        BigInteger id = insertTrans(withdrawDTO, authorization, account);
        // 调用提现接口,异步
        sendTrans(withdrawDTO, authorization, id, account);
    }

    @Async
    public void sendTrans(WithdrawDTO withdrawDTO, String authorization, BigInteger id, Result<JSONObject> account) {
        SendTransactionDTO sendTransactionDTO = new SendTransactionDTO();
        sendTransactionDTO.setPass(account.getData().getString("password"));
        sendTransactionDTO.setTo(withdrawDTO.getAddress());
        sendTransactionDTO.setFrom(account.getData().getString("address"));
        BigInteger fee = ConfigUtil.withdrawConfigMap.get(withdrawDTO.getType()).getPoundageValue(withdrawDTO.getValue(), withdrawDTO.getType());
        sendTransactionDTO.setValue(CoinUtil.Value2wei(withdrawDTO.getValue(), withdrawDTO.getType()).subtract(fee));
        Result result = ethernumService.approveAndCall(CoinUtil.getAddress(withdrawDTO.getType()), sendTransactionDTO, authorization);
        Assert.isTrue(result.getCode() == ResultCode.SUCCESS.code, result.getMessage());
        Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setTxHash(result.getData().toString());
        transaction.setStatus(1);
        transactionMapper.updateByPrimaryKeySelective(transaction);
        capitalService.updateBalance(transaction.getCoinId(), transaction.getUserId(), BigInteger.ZERO.subtract(transaction.getQuantity()));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public BigInteger insertTrans(WithdrawDTO withdrawDTO, String authorization, Result<JSONObject> account) throws UnsupportedEncodingException {
        Long seq = redisTemplate.opsForValue().increment("TRANSATION_C", 1);
        BigInteger fee = ConfigUtil.withdrawConfigMap.get(withdrawDTO.getType()).getPoundageValue(withdrawDTO.getValue(), withdrawDTO.getType());
        Transaction transaction = new Transaction();
        transaction.setActualQuantity(CoinUtil.Value2wei(withdrawDTO.getValue(), withdrawDTO.getType()).subtract(fee));
        transaction.setCoinId(CoinUtil.getId(withdrawDTO.getType()));
        transaction.setQuantity(CoinUtil.Value2wei(withdrawDTO.getValue(), withdrawDTO.getType()));
        transaction.setType(1);
        transaction.setStatus(0);
        transaction.setUserId(new BigInteger(BaseContextHandler.getUserID()));
        transaction.setOrderId("T" + String.format("%09d", seq));
        transaction.setFromAddress(account.getData().getString("address"));
        transaction.setToAddress(withdrawDTO.getAddress());
        insert(transaction);
        return transaction.getId();
    }

    private void checkPwd(WithdrawDTO withdrawDTO, String authorization) {
        PwdCheckDTO dto = new PwdCheckDTO();
        dto.setUserId(BaseContextHandler.getUserIDInt());
        dto.setPassword(withdrawDTO.getPassword());
        Result result = userService.checkPwd(dto, authorization);
        Assert.isTrue(ObjectUtils.equals(result.getCode(), ResultCode.SUCCESS.code), result.getMessage());
    }

    private void checkSms(WithdrawDTO withdrawDTO) {
        MobileValiDTO mobileValiDTO = new MobileValiDTO();
        mobileValiDTO.setValiCode(withdrawDTO.getValiCode());
        mobileValiDTO.setMobile(BaseContextHandler.getUsername());
        ResponseEntity<Boolean> checkResult = smsService.checkSms(mobileValiDTO);
        Assert.isTrue(checkResult.getBody(), "验证码错误");
    }

    private void checkWithdraw(WithdrawDTO withdrawDTO) {
        boolean withdrawSwitch = ConfigUtil.withdrawConfigMap.get(withdrawDTO.getType()).getSwitchKey() == 1;
        Assert.isTrue(withdrawSwitch, "系统错误, 请稍后再试");
        // 提现金额大于最小提现金额
        org.springframework.util.Assert.isTrue(withdrawDTO.getValue().compareTo(ConfigUtil.withdrawConfigMap.get(withdrawDTO.getType()).getMin()) >= 0, "提现金额过小");
        // 累计金额 - 锁仓金额 > 当天剩余额度
        Capital capital = new Capital();
        capital.setUserId(BaseContextHandler.getUserIDInt());
        capital.setCoinId(CoinUtil.getId(withdrawDTO.getType()));
        Capital capitalTemp = capitalService.selectOne(capital);
        BigInteger max = CoinUtil.Value2wei(ConfigUtil.withdrawConfigMap.get(withdrawDTO.getType()).getMax(), withdrawDTO.getType());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        BigInteger cost = transactionMapper.costByDay(BaseContextHandler.getUserIDInt(), CoinUtil.getId(withdrawDTO.getType()), df.format(System.currentTimeMillis()));
        BigInteger balance = capitalTemp.getBalance().subtract(capitalTemp.getLocked());
        BigInteger value = CoinUtil.Value2wei(withdrawDTO.getValue(), withdrawDTO.getType());
        Assert.notNull(capital, "余额不足");
        Assert.isTrue(balance.subtract(value).compareTo(BigInteger.ZERO) >= 0, "余额不足");

    }

    public void deposite(DepositeDTO depositeDTO) throws UnsupportedEncodingException {
        // 校验密码
        Transaction transaction = new Transaction();
        transaction.setActualQuantity(CoinUtil.Value2wei(depositeDTO.getValue(), depositeDTO.getType()));
        transaction.setCoinId(CoinUtil.getId(depositeDTO.getType()));
        transaction.setQuantity(CoinUtil.Value2wei(depositeDTO.getValue(), depositeDTO.getType()));
        transaction.setType(0);
        transaction.setStatus(0);
        transaction.setUserId(new BigInteger(BaseContextHandler.getUserID()));
        transaction.setOrderId("0x");
        transaction.setFromAddress("0x");
        transaction.setToAddress("0x");
        insert(transaction);

    }

    public Double getLimit(String type) {
        Capital capital = new Capital();
        capital.setUserId(BaseContextHandler.getUserIDInt());
        capital.setCoinId(CoinUtil.getId(type));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        BigInteger cost = transactionMapper.costByDay(BaseContextHandler.getUserIDInt(), CoinUtil.getId(type), df.format(System.currentTimeMillis()));
        return CoinUtil.wei2Value(CoinUtil.getId(type), cost);

    }
}
