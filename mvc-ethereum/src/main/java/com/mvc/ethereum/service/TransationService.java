package com.mvc.ethereum.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mvc.common.biz.BaseBiz;
import com.mvc.common.constant.CommonConstants;
import com.mvc.common.context.BaseContextHandler;
import com.mvc.common.dto.TransactionDTO;
import com.mvc.ethereum.configuration.WalletConfig;
import com.mvc.ethereum.mapper.CapitalMapper;
import com.mvc.ethereum.mapper.TransactionMapper;
import com.mvc.ethereum.model.Transaction;
import com.mvc.ethereum.model.vo.*;
import com.mvc.ethereum.utils.CoinUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.quorum.Quorum;
import org.web3j.quorum.methods.request.PrivateTransaction;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.web3j.tx.Contract.GAS_LIMIT;
import static org.web3j.tx.ManagedTransaction.GAS_PRICE;

/**
 * ${DESCRIPTION}
 *
 * @author wanghaobin
 * @create 2017-06-08 16:23
 */
@Service
@Transactional(rollbackFor = Exception.class)
@lombok.extern.java.Log
public class TransationService extends BaseBiz<TransactionMapper, Transaction> {

    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private Admin admin;
    @Autowired
    private Quorum quorum;
    @Autowired
    private WalletConfig walletConfig;
    @Autowired
    private CapitalMapper capitalMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    // 这里到时候需要通过mq来调用, 以提高速度和安全性
    public Boolean insetTransaction(Transaction transaction) {
        transactionMapper.insert(transaction);
        return true;
    }

    public void insertList(List<Transaction> transactions) {
        transactions.stream().forEach(obj -> transactionMapper.insert(obj));
    }

    @Async
    public void insert(org.web3j.protocol.core.methods.request.Transaction transaction, String pass, String orderId, String contractAddress) throws Exception {
        EthSendTransaction result = eth_sendTransaction(transaction, pass, contractAddress);
        Integer status = result.hasError() ? 9 : 1;
        // 更新状态
        transactionMapper.updateByOrderId(orderId, result.getTransactionHash(), status);
    }

    public EthSendTransaction eth_sendTransaction(org.web3j.protocol.core.methods.request.Transaction transaction, String pass, String contractAddress) throws Exception {
        try {
            PersonalUnlockAccount flag = admin.personalUnlockAccount(transaction.getFrom(), pass).send();
            Assert.isTrue(flag.accountUnlocked(), "unlock error");
        } catch (IOException e) {
            log.info("unlock error");
        }
        Function function = new Function("transfer", Arrays.<Type>asList(new Address(transaction.getTo()), new Uint256(Numeric.decodeQuantity(transaction.getValue()))), Collections.<TypeReference<?>>emptyList());
        String data = FunctionEncoder.encode(function);
        PrivateTransaction privateTransaction = new PrivateTransaction(transaction.getFrom(), null, GAS_LIMIT, contractAddress, BigInteger.ZERO, data, Arrays.asList(transaction.getFrom(), transaction.getTo(), contractAddress));
        EthSendTransaction response = quorum.ethSendTransaction(privateTransaction).send();

        return response;
    }

    public EthSendTransaction eth_sendTransaction(org.web3j.protocol.core.methods.request.Transaction transaction, String pass) throws Exception {
        PersonalUnlockAccount flag = admin.personalUnlockAccount(transaction.getFrom(), pass).send();
        Assert.isTrue(flag.accountUnlocked(), "unlock error");
        EthSendTransaction response = admin.ethSendTransaction(transaction).send();
        return response;
    }

    public void updateByHash(String hash) {
        // 更新状态
        transactionMapper.updateByHash(hash);
    }

    public PageInfo<TransactionVO> transactions(TransactionDTO transactionDTO) {
        PageHelper.startPage(transactionDTO.getPageNo(), transactionDTO.getPageSize());
        List<TransactionVO> list = transactionMapper.list(transactionDTO);
        return new PageInfo<>(list);
    }

    public AdminBalanceVO getAdminBalance(String type, String timeUnit) {
        BigInteger coinId = CoinUtil.getId(type);
        String unit = null;
        switch (timeUnit) {
            case "day":
                unit = "DATE_SUB(CURDATE(), INTERVAL 1 DAY)";
                break;
            case "week":
                unit = "DATE_SUB(CURDATE(), INTERVAL 1 WEEK)";
                break;
            case "month":
                unit = "DATE_SUB(CURDATE(), INTERVAL 1 MONTH)";
                break;
            case "all":
                unit = "0";
                break;
            default:
                unit = "0";
        }
        WithdrawCount withdrawCount = transactionMapper.withdrawCount(coinId, unit);
        DepositeCount depositeCount = transactionMapper.depositeCount(coinId, unit);
        JSONObject all = transactionMapper.lockCount(coinId, unit, "0,1");
        JSONObject now = transactionMapper.lockCount(coinId, unit, "0");
        JSONObject unlock = transactionMapper.lockCount(coinId, unit, "1");
        LockCount lockCount = setValue(all, now, unlock);
        AdminBalanceVO adminBalanceVO = new AdminBalanceVO(BigInteger.ZERO, withdrawCount, depositeCount, lockCount, coinId);
        return adminBalanceVO;
    }

    private LockCount setValue(JSONObject all, JSONObject now, JSONObject unlock) {
        LockCount lockCount = new LockCount();
        lockCount.setAllBalance(all.getBigInteger("balance"));
        lockCount.setNowBalance(now.getBigInteger("balance"));
        lockCount.setUnlockBalance(unlock.getBigInteger("balance"));
        lockCount.setAllInterest(all.getBigInteger("interest"));
        lockCount.setNowInterest(now.getBigInteger("interest"));
        lockCount.setUnlockInterest(unlock.getBigInteger("interest"));
        lockCount.setAllNum(all.getInteger("num"));
        lockCount.setNowNum(now.getInteger("num"));
        lockCount.setUnlockNum(unlock.getInteger("num"));
        lockCount.setCoinId(all.getBigInteger("coin_id"));
        return lockCount;
    }

    @Async
    public void update(Log tx) {
        String from = getFrom(tx);
        String to = getTo(tx);
        String hash = tx.getTransactionHash();
        BigInteger coinId = CoinUtil.getId(walletConfig.getType(tx.getAddress()));
        BigInteger value = getValue(tx.getData());
        Transaction transactionTemp = new Transaction();
        transactionTemp.setTxHash(hash);
        Transaction transaction = transactionMapper.selectOne(transactionTemp);
        if (null != transaction && transaction.getStatus().equals(CommonConstants.SUCCESS) ) {
            return;
        }
        if (null == transaction) {
            // 其他账户 -> 用户临时账户
            transaction = newTransfaction(tx);
            if (null != transaction.getUserId()) {
                transactionMapper.insert(transaction);
                // 插入成功后转移至总账户
                org.web3j.protocol.core.methods.request.Transaction trans = null;
                try {
                    trans = buildTransaction(from, to, value);
                    EthSendTransaction result = eth_sendTransaction(trans, walletConfig.getPass(tx.getAddress()), tx.getAddress());
                    System.out.println(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // 更新余额信息


                capitalMapper.updateBalance(coinId, transaction.getUserId(), value.toString());
            }
        } else if (walletConfig.isWalletAccount(from)) {
            // 总账户 -> 其他账户
            transactionMapper.updateByHash(hash);
            if (null != transaction.getUserId()) {
                // 更新余额信息
                capitalMapper.updateBalance(coinId, transaction.getUserId(), BigInteger.ZERO.subtract(value).toString(10));
            }
        } else if (walletConfig.isWalletAccount(to)) {
            // 用户临时账户 -> 总账户
            transactionMapper.updateByHash(hash);
            // 更新余额信息
            capitalMapper.updateBalance(coinId, transaction.getUserId(), value.toString(10));
        }

    }

    private Transaction newTransfaction(Log tx) {
        BigInteger coinId = CoinUtil.getId(walletConfig.getType(tx.getAddress()));
        BigInteger userId = (BigInteger) redisTemplate.opsForValue().get(BaseContextHandler.ADDR_LISTEN_ + getTo(tx));
        Long seq = redisTemplate.opsForValue().increment("TRANSATION_C", 1);
        Transaction transaction = new Transaction();
        transaction.setTxHash(tx.getTransactionHash());
        transaction.setCoinId(coinId);
        transaction.setUserId(userId);
        // 充值
        transaction.setType(0);
        transaction.setToAddress(getTo(tx));
        transaction.setStatus(1);
        transaction.setQuantity(getValue(tx.getData()));
        transaction.setActualQuantity(getValue(tx.getData()));
        transaction.setFromAddress(getFrom(tx));
        transaction.setOrderId("C" + String.format("%09d", seq));
        return transaction;
    }

    private String getFrom(Log tx) {
        return "0x" + tx.getTopics().get(1).substring(26);
    }

    private String getTo(Log tx) {
        return "0x" + tx.getTopics().get(2).substring(26);
    }


    private BigInteger getValue(String data) {
        BigInteger value = new BigInteger(data.replace("0x", ""), 16);
        return value;
    }

    public org.web3j.protocol.core.methods.request.Transaction buildTransaction(String to, String from, BigInteger value) throws Exception {
        org.web3j.protocol.core.methods.request.Transaction transaction = new org.web3j.protocol.core.methods.request.Transaction(
                from,
                null,
                GAS_PRICE.divide(BigInteger.valueOf(10)),
                GAS_LIMIT.divide(BigInteger.valueOf(10)),
                to,
                value,
                null
        );
        return transaction;
    }

}
