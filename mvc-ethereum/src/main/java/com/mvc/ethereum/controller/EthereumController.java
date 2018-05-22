package com.mvc.ethereum.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.mvc.common.dto.BatchTransferDTO;
import com.mvc.common.dto.LockRecordDTO;
import com.mvc.common.dto.TransactionDTO;
import com.mvc.common.msg.Result;
import com.mvc.common.msg.ResultGenerator;
import com.mvc.common.util.RSACoder;
import com.mvc.ethereum.model.dto.*;
import com.mvc.ethereum.model.vo.AdminBalanceVO;
import com.mvc.ethereum.model.vo.LockRecordVO;
import com.mvc.ethereum.model.vo.TransactionVO;
import com.mvc.ethereum.service.RpcService;
import com.mvc.ethereum.utils.FileUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.utils.Convert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * @author qyc
 */
@RestController
@RequestMapping("ethereum")
public class EthereumController {

    @Autowired
    private RpcService rpcService;

    /**
     * getBalance
     *
     * @param balanceDTO
     * @return
     */
    @RequestMapping(value = "eth_getBalance", method = RequestMethod.POST)
    public Object eth_getBalance(HttpServletRequest request, @RequestBody final BalanceDTO balanceDTO) throws Exception {
        return rpcService.eth_getBalance(balanceDTO.getAddress(), balanceDTO.getBlockId());
    }

    /**
     * getTransactionByHash
     *
     * @return
     */
    @RequestMapping(value = "eth_getTransactionByHash", method = RequestMethod.POST)
    public Object eth_getTransactionByHash(@RequestBody TransactionByHashDTO transactionByHashDTO) throws Exception {
        return rpcService.eth_getTransactionByHash(transactionByHashDTO.getTransactionHash());
    }

    /**
     * sendRawTransaction
     *
     * @param rawTransactionDTO
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "eth_sendRawTransaction", method = RequestMethod.POST)
    public Object ethSendRawTransaction(@RequestBody RawTransactionDTO rawTransactionDTO) throws Exception {
        return rpcService.ethSendRawTransaction(rawTransactionDTO.getSignedMessage());
    }

    /**
     * sendTransaction
     *
     * @param sendTransactionDTO
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "eth_sendTransaction", method = RequestMethod.POST)
    public Object eth_sendTransaction(@RequestBody SendTransactionDTO sendTransactionDTO) throws Exception {
        Transaction transaction = new Transaction(sendTransactionDTO.getFrom(), sendTransactionDTO.getNonce(), sendTransactionDTO.getGasPrice(), sendTransactionDTO.getGas(), sendTransactionDTO.getTo(),
                Convert.toWei(sendTransactionDTO.getValue(), Convert.Unit.ETHER).toBigInteger(),
                sendTransactionDTO.getData());
        return rpcService.eth_sendTransaction(transaction, sendTransactionDTO.getPass());
    }

    /**
     * search user list
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "personal_listAccounts", method = RequestMethod.POST)
    public Object personal_listAccounts() throws Exception {
        return rpcService.personal_listAccounts();
    }

    /**
     * create new Account
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "personal_newAccount", method = RequestMethod.POST)
    public Object personal_newAccount(@RequestBody NewAccountDTO newAccountDTO) throws Exception {
        return ResultGenerator.genSuccessResult(rpcService.personal_newAccount(newAccountDTO.getPassphrase()));
    }

    /**
     * import key
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "personal_importRawKey", method = RequestMethod.POST)
    public Object personal_importRawKey(@RequestBody ImportRawKeyDTO importRawKeyDTO) throws Exception {
        return rpcService.personal_importRawKey(importRawKeyDTO.getKeydata(), importRawKeyDTO.getPassphrase());
    }

    /**
     * get publickey for RSA
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "publicKey", method = RequestMethod.POST)
    public Object publicKey() throws Exception {
        return RSACoder.getPublicKey();
    }

    /**
     * get TransactionCount for nonce
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "transactionCount", method = RequestMethod.POST)
    public Object getTransactionCount(@RequestBody TransactionCountDTO transactionCountDTO) throws Exception {
        return rpcService.getTransactionCount(transactionCountDTO.getAddress());
    }

    /**
     * personal by keyDate
     *
     * @param file
     * @param passhphrase
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "personalByKeyDate", method = RequestMethod.POST)
    public Object eth_personalByKeyDate(@RequestParam("file") MultipartFile file, @RequestParam String passhphrase) throws Exception {
        String source = FileUtil.readFile(file.getInputStream());
        return rpcService.eth_personalByKeyDate(source, passhphrase);
    }

    /**
     * personal by privateKey
     *
     * @param personalByPrivateKeyDTO
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "personalByPrivateKey", method = RequestMethod.POST)
    public Object eth_personalByPrivateKey(@RequestBody PersonalByPrivateKeyDTO personalByPrivateKeyDTO) throws Exception {
        return rpcService.eth_personalByPrivateKey(personalByPrivateKeyDTO.getPrivateKey());
    }

    /**
     * 自动转账
     */
    @RequestMapping(value = "batchTransfer", method = RequestMethod.POST)
    public Result batchTransfer(@RequestBody @Valid BatchTransferDTO batchTransferDTO) throws Exception {
        rpcService.insertBatch(batchTransferDTO);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 查询余额
     *
     * @param transactionDTO
     * @return
     */
    @RequestMapping(value = "transactions", method = RequestMethod.GET)
    public Result transaction(@ModelAttribute @Valid TransactionDTO transactionDTO) {
        PageInfo<TransactionVO> result = rpcService.transactions(transactionDTO);
        return ResultGenerator.genSuccessResult(result);
    }

    /**
     * 锁仓
     *
     * @param lockRecordDTO
     * @return
     */
    @RequestMapping(value = "lock_record", method = RequestMethod.GET)
    public Result lock_record(@ModelAttribute @Valid LockRecordDTO lockRecordDTO) {
        PageInfo<LockRecordVO> result = rpcService.lock_record(lockRecordDTO);
        return ResultGenerator.genSuccessResult(result);
    }

    @RequestMapping(value = "all_balance", method = RequestMethod.GET)
    public Result balance(@RequestParam("type") String type, @RequestParam("timeUnit") String timeUnit) throws Exception {
        AdminBalanceVO adminBalanceVO = rpcService.getAdminBalance(type, timeUnit);
        return ResultGenerator.genSuccessResult(adminBalanceVO);
    }

    @RequestMapping(value = "wallet_account", method = RequestMethod.GET)
    public Result<JSONObject> getAccount(@RequestParam("type") String type) throws Exception {
        JSONObject address = rpcService.getAccount(type);
        return ResultGenerator.genSuccessResult(address);
    }

    @ApiOperation("导入账户")
    @PostMapping(value = "import/account")
    public Result<Long> importAccount(@RequestBody MultipartFile file) throws Exception {
        String jsonStr = IOUtils.toString(file.getInputStream());
        List<Map> list = JSON.parseArray(jsonStr, Map.class);
        rpcService.importAccount(list);
        return ResultGenerator.genSuccessResult();
    }

    @ApiOperation("获取剩余账户数量, 过少则需要新增导入")
    @GetMapping(value = "account/size")
    public Result<Long> getAccountSize() throws Exception {
        Long size = rpcService.getAccountSize();
        return ResultGenerator.genSuccessResult(size);
    }

    @ApiOperation("下载待处理数据-所有")
    @GetMapping("all/json")
    void getAllJson(HttpServletResponse response) throws IOException {
        List<Orders> accountList = rpcService.getTransactionJson("all");
        response.setContentType("text/plain");
        response.addHeader("Content-Disposition", "attachment; filename=" + String.format("all_%s.json", System.currentTimeMillis()));
        OutputStream os = response.getOutputStream();
        BufferedOutputStream buff = new BufferedOutputStream(os);
        buff.write(JSON.toJSONString(accountList).getBytes("UTF-8"));
        buff.flush();
        buff.close();
    }

    @ApiOperation("下载待处理数据-交易")
    @GetMapping("transaction/json")
    void getTransactionJson(HttpServletResponse response) throws IOException {
        List<Orders> accountList = rpcService.getTransactionJson("transaction");
        response.setContentType("text/plain");
        response.addHeader("Content-Disposition", "attachment; filename=" + String.format("transaction_%s.json", System.currentTimeMillis()));
        OutputStream os = response.getOutputStream();
        BufferedOutputStream buff = new BufferedOutputStream(os);
        buff.write(JSON.toJSONString(accountList).getBytes("UTF-8"));
        buff.flush();
        buff.close();
    }

    @ApiOperation("下载待处理数据-汇总")
    @GetMapping("collect/json")
    void getCollectionJson(HttpServletResponse response) throws IOException {
        List<Orders> accountList = rpcService.getTransactionJson("collect");
        response.setContentType("text/plain");
        response.addHeader("Content-Disposition", "attachment; filename=" + String.format("collect_%s.json", System.currentTimeMillis()));
        OutputStream os = response.getOutputStream();
        BufferedOutputStream buff = new BufferedOutputStream(os);
        buff.write(JSON.toJSONString(accountList).getBytes("UTF-8"));
        buff.flush();
        buff.close();
    }

    @ApiOperation("导入待处理交易")
    @PostMapping(value = "import/transaction")
    public Result<Long> importTransaction(@RequestBody MultipartFile file) throws Exception {
        String jsonStr = IOUtils.toString(file.getInputStream());
        List<Map> list = JSON.parseArray(jsonStr, Map.class);
        rpcService.importTransaction(list);
        return ResultGenerator.genSuccessResult();
    }
    
}
