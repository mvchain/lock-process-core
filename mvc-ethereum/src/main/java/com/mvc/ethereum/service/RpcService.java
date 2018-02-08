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
 * @author qyc
 */
public interface RpcService {

    /**
     * eth_getBalance
     *
     * @param address
     * @param blockId
     * @return
     * @throws Exception
     */
    Object eth_getBalance(String address, String blockId) throws Exception;

    /**
     * eth_getTransactionByHash
     *
     * @param transactionHash
     * @return
     * @throws Exception
     */
    Object eth_getTransactionByHash(String transactionHash) throws Exception;

    /**
     * eth_sendTransaction
     *
     * @param transaction
     * @param pass
     * @return
     * @throws Exception
     */
    EthSendTransaction eth_sendTransaction(Transaction transaction, String pass) throws Exception;

    /**
     * personal_newAccount
     *
     * @param passhphrase
     * @return
     * @throws Exception
     */
    Object personal_newAccount(String passhphrase) throws Exception;

    /**
     * personal_listAccounts
     *
     * @return
     * @throws IOException
     */
    Object personal_listAccounts() throws IOException;

    /**
     * personal_importRawKey
     *
     * @param keydata
     * @param passphrase
     * @return
     * @throws Exception
     */
    Object personal_importRawKey(String keydata, String passphrase) throws Exception;

    /**
     * ethSendRawTransaction
     *
     * @param signedMessage
     * @return
     * @throws Exception
     */
    Object ethSendRawTransaction(String signedMessage) throws Exception;

    /**
     * getTransactionCount
     *
     * @param address
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    Object getTransactionCount(String address) throws ExecutionException, InterruptedException;

    /**
     * eth_personalByKeyDate
     *
     * @param source
     * @param passhphrase
     * @return
     * @throws Exception
     */
    Object eth_personalByKeyDate(String source, String passhphrase) throws Exception;

    /**
     * eth_personalByPrivateKey
     *
     * @param privateKey
     * @return
     * @throws Exception
     */
    Object eth_personalByPrivateKey(String privateKey) throws Exception;

    /**
     * eth_sendTransaction
     *
     * @param transaction
     * @param pass
     * @param contractAddress
     * @return
     * @throws Exception
     */
    Object eth_sendTransaction(Transaction transaction, String pass, String contractAddress) throws Exception;

    /**
     * txList
     *
     * @param address
     * @return
     */
    Object txList(String address);

    /**
     * insertBatch
     *
     * @param batchTransferDTO
     * @throws Exception
     */
    void insertBatch(BatchTransferDTO batchTransferDTO) throws Exception;

    /**
     * transactions
     *
     * @param transactionDTO
     * @return
     */
    PageInfo<TransactionVO> transactions(TransactionDTO transactionDTO);

    /**
     * lock_record
     *
     * @param lockRecordDTO
     * @return
     */
    PageInfo<LockRecordVO> lock_record(LockRecordDTO lockRecordDTO);

    /**
     * getAdminBalance
     *
     * @param type
     * @param timeUnit
     * @return
     * @throws Exception
     */
    AdminBalanceVO getAdminBalance(String type, String timeUnit) throws Exception;

    /**
     * getAccount
     *
     * @param type
     * @return
     */
    JSONObject getAccount(String type);
}
