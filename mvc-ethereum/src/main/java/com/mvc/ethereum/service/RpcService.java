package com.mvc.ethereum.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.mvc.common.dto.BatchTransferDTO;
import com.mvc.common.dto.LockRecordDTO;
import com.mvc.common.dto.TransactionDTO;
import com.mvc.ethereum.model.vo.AdminBalanceVO;
import com.mvc.ethereum.model.vo.LockRecordVO;
import com.mvc.ethereum.model.vo.TransactionVO;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthSendTransaction;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * remote service
 */
public interface RpcService {

    Object eth_getBalance(String address, String blockId) throws Exception;

    Object eth_getTransactionByHash(String transactionHash) throws Exception;

    EthSendTransaction eth_sendTransaction(Transaction transaction, String pass) throws Exception;

    Object personal_newAccount(String passhphrase) throws Exception;

    Object personal_listAccounts() throws IOException;

    Object personal_importRawKey(String keydata, String passphrase) throws Exception;

    Object parityExportAccount(String address, String passphrase) throws IOException;

    Object ethSendRawTransaction(String signedMessage) throws Exception;

    Object getTransactionCount(String address) throws ExecutionException, InterruptedException;

    Object eth_personalByKeyDate(String source, String passhphrase) throws Exception;

    Object eth_personalByPrivateKey(String privateKey) throws Exception;

    Object eth_sendTransaction(Transaction transaction, String pass, String contractAddress) throws Exception;

    Object txList(String address);

    void insertBatch(BatchTransferDTO batchTransferDTO) throws Exception;

    PageInfo<TransactionVO> transactions(TransactionDTO transactionDTO);

    PageInfo<LockRecordVO> lock_record(LockRecordDTO lockRecordDTO);

    AdminBalanceVO getAdminBalance(String type, String timeUnit) throws Exception;

    JSONObject getAccount(String type);
}
