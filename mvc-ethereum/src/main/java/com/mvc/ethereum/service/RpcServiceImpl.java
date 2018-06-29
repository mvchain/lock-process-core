package com.mvc.ethereum.service;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import com.mvc.common.dto.BatchTransferDTO;
import com.mvc.common.dto.LockRecordDTO;
import com.mvc.common.dto.TransactionDTO;
import com.mvc.common.dto.TransferDTO;
import com.mvc.ethereum.configuration.WalletConfig;
import com.mvc.ethereum.constant.EthConstants;
import com.mvc.ethereum.controller.Orders;
import com.mvc.ethereum.model.JsonCredentials;
import com.mvc.ethereum.model.TransactionResponse;
import com.mvc.ethereum.model.vo.AdminBalanceVO;
import com.mvc.ethereum.model.vo.LockRecordVO;
import com.mvc.ethereum.model.vo.TransactionVO;
import com.mvc.ethereum.utils.CoinUtil;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
import org.web3j.protocol.admin.methods.response.PersonalListAccounts;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.geth.Geth;
import org.web3j.quorum.Quorum;
import org.web3j.quorum.methods.request.PrivateTransaction;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static org.web3j.tx.Contract.GAS_LIMIT;
import static org.web3j.utils.Convert.Unit;
import static org.web3j.utils.Convert.fromWei;

/**
 * @author qyc
 */
@Log
@Component
public class RpcServiceImpl implements RpcService {
    @Autowired
    private Admin admin;
    @Autowired
    private Geth geth;
    @Autowired
    private Web3j web3j;
    @Autowired
    private Quorum quorum;
    @Value("${trans.log.url}")
    String transLogUrl;
    @Value("${wallet.coldUser}")
    String coldUser;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private TransationService transationService;
    @Autowired
    private WalletConfig walletConfig;
    @Autowired
    private LockRecordService lockRecordService;
    @Autowired
    private ContractService contractService;

    final static String LOCK_PLAT_TRANS_TEMP = "LOCK_PLAT_TRANS_TEMP";
    final static String LOCK_PLAT_TRANS = "LOCK_PLAT_TRANS";
    final static String LOCK_PLAT_USER = "LOCK_PLAT_USER";
    final static String LOCK_PLAT_TRANS_LIST = "LOCK_PLAT_TRANS_LIST";

    @Override
    public Object eth_personalByKeyDate(String source, String passhphrase) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        WalletFile file = objectMapper.readValue(source, WalletFile.class);
        ECKeyPair ecKeyPair = Wallet.decrypt(passhphrase, file);
        Credentials credentials = Credentials.create(ecKeyPair);
        return new JsonCredentials(credentials);
    }

    @Override
    public Object eth_personalByPrivateKey(String privateKey) throws Exception {
        return new JsonCredentials(Credentials.create(privateKey));
    }

    @Override
    public Object eth_getBalance(String address, String blockId) throws Exception {
        EthGetBalance response = web3j.ethGetBalance(address, DefaultBlockParameter.valueOf(blockId)).send();
        BigDecimal result = fromWei(String.valueOf(response.getBalance()), Unit.ETHER);
        return result;
    }

    @Override
    public Object eth_getTransactionByHash(String transactionHash) throws Exception {
        EthTransaction response = web3j.ethGetTransactionByHash(transactionHash).send();
        return response;
    }

    @Override
    public EthSendTransaction eth_sendTransaction(Transaction transaction, String pass) throws Exception {
        PersonalUnlockAccount flag = admin.personalUnlockAccount(transaction.getFrom(), pass).send();
        Assert.isTrue(flag.accountUnlocked(), "unlock error");
        EthSendTransaction response = admin.ethSendTransaction(transaction).send();
        return response;
    }

    private <T, R> TransactionResponse<R> processEventResponse(
            List<T> eventResponses, TransactionReceipt transactionReceipt, java.util.function.Function<T, R> map) {
        if (!eventResponses.isEmpty()) {
            return new TransactionResponse<>(
                    transactionReceipt.getTransactionHash(),
                    map.apply(eventResponses.get(0)));
        } else {
            return new TransactionResponse<>(
                    transactionReceipt.getTransactionHash());
        }
    }

    @Override
    public Object eth_sendTransaction(Transaction transaction, String pass, String contractAddress) throws Exception {
        PersonalUnlockAccount flag = admin.personalUnlockAccount(transaction.getFrom(), pass).send();
        Assert.isTrue(flag.accountUnlocked(), "unlock error");
        Function function = new Function("transfer", Arrays.<Type>asList(new Address(transaction.getTo()), new Uint256(Numeric.decodeQuantity(transaction.getValue()))), Collections.<TypeReference<?>>emptyList());
        String data = FunctionEncoder.encode(function);
        PrivateTransaction privateTransaction = new PrivateTransaction(transaction.getFrom(), null, GAS_LIMIT.divide(BigInteger.valueOf(100)), contractAddress, BigInteger.ZERO, data, Arrays.asList(transaction.getFrom(), transaction.getTo(), contractAddress));
        EthSendTransaction response = quorum.ethSendTransaction(privateTransaction).send();
        io.jsonwebtoken.lang.Assert.isTrue(!response.hasError(), "eth_sendTransaction error");
        return response.getTransactionHash();
    }

    @Override
    public Object ethSendRawTransaction(String signedMessage) throws Exception {
        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(signedMessage).send();
        return ethSendTransaction;
    }

    @Override
    public Object personal_newAccount(String passhphrase) throws Exception {
        NewAccountIdentifier response = admin.personalNewAccount(passhphrase).send();
        geth.personalUnlockAccount(response.getAccountId(), passhphrase).send();
        return response.getResult();
    }

    @Override
    public Object personal_listAccounts() throws IOException {
        PersonalListAccounts response = geth.personalListAccounts().send();
        return response;
    }

    @Override
    public Object personal_importRawKey(String keydata, String passphrase) throws Exception {
        return geth.personalImportRawKey(keydata, passphrase).send();
    }

    public Object parityExportAccount(String address, String passphrase) throws IOException {
        return null;
    }

    @Override
    public Object getTransactionCount(String address) throws ExecutionException, InterruptedException {
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).sendAsync().get();
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();
        return nonce;
    }

    @Override
    public Object txList(String address) {


        String url = transLogUrl + String.format(EtherscanUrl.TXLIST, address);
        ResponseEntity<String> response = new RestTemplate().exchange(url, HttpMethod.GET, null, String.class);
        System.out.println(response.getHeaders());
        return response.getBody();
    }

    @Override
    public void insertBatch(BatchTransferDTO batchTransferDTO) throws Exception {
        // 同步批量插入
        List<com.mvc.ethereum.model.Transaction> transactions = new ArrayList<>(batchTransferDTO.getTransferDTOS().size());
        for (int i = 0; i < batchTransferDTO.getTransferDTOS().size(); i++) {
            TransferDTO transferDTO = batchTransferDTO.getTransferDTOS().get(i);
            Long seq = redisTemplate.opsForValue().increment("TRANSATION_T", 1);
            com.mvc.ethereum.model.Transaction transaction = new com.mvc.ethereum.model.Transaction();
            transaction.setActualQuantity(CoinUtil.Value2wei(BigDecimal.valueOf(transferDTO.getValue()), transferDTO.getType()));
            transaction.setQuantity(transaction.getActualQuantity());
            transaction.setCoinId(CoinUtil.getId(transferDTO.getType()));
            transaction.setType(2);
            transaction.setStatus(0);
            transaction.setOrderId("C" + String.format("%09d", seq));
            transaction.setFromAddress(walletConfig.getAccount(batchTransferDTO.getType()));
            transaction.setToAddress(transferDTO.getAddress());
            transactions.add(transaction);
        }
        transationService.insertList(transactions);
        // 异步插入
        for (com.mvc.ethereum.model.Transaction trans : transactions) {
            Transaction transaction = buildTransaction(trans.getToAddress(), trans.getFromAddress(), trans.getActualQuantity());
            transationService.insert(
                    transaction,
                    walletConfig.getPass(batchTransferDTO.getType(), null),
                    trans.getOrderId(), CoinUtil.getContractAddress(batchTransferDTO.getType()));
        }
    }

    @Override
    public PageInfo<TransactionVO> transactions(TransactionDTO transactionDTO) {
        return transationService.transactions(transactionDTO);
    }

    @Override
    public PageInfo<LockRecordVO> lock_record(LockRecordDTO lockRecordDTO) {
        return lockRecordService.list(lockRecordDTO);
    }

    @Override
    public AdminBalanceVO getAdminBalance(String type, String timeUnit) throws Exception {
        AdminBalanceVO balanceVO = transationService.getAdminBalance(type, timeUnit);
        BigInteger balance = contractService.balanceOf(walletConfig.getAddress(type), walletConfig.getAccount(type));
        balanceVO.setBalance(balance);
        BigInteger nowBalance = (BigInteger) redisTemplate.opsForValue().get(EthConstants.OTHER_BALANCE);
        nowBalance = nowBalance == null ? BigInteger.ZERO : nowBalance;
        balanceVO.setOtherBalance(nowBalance);
        return balanceVO;
    }

    @Override
    public JSONObject getAccount(String type) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("address", coldUser);
        jsonObject.put("password", walletConfig.getPass(type, null));
        return jsonObject;
    }

    @Override
    public void reTransaction() {
        com.mvc.ethereum.model.Transaction transaction = transationService.selectWaitTrans(BigInteger.ZERO);
        while (null != transaction) {
            try {
                EthGetTransactionReceipt tx = web3j.ethGetTransactionReceipt(transaction.getTxHash()).send();
                if ("0x0".equals(tx.getTransactionReceipt().get().getStatus())) {
                    continue;
                }
                String type = CoinUtil.coinMap.get(transaction.getCoinId()).getAbbr();
                Transaction trans = buildTransaction(transaction.getToAddress(), transaction.getFromAddress(), transaction.getActualQuantity());
                Object result = eth_sendTransaction(trans, walletConfig.getPass(type, null), walletConfig.getAddress(type));
                if (null != result) {
                    transaction.setTxHash(result.toString());
                    transationService.update(transaction);
                }
                transaction = transationService.selectWaitTrans(transaction.getId());
            } catch (Exception e) {
                continue;
            }
        }
    }

    private Transaction buildTransaction(String to, String from, BigInteger value) throws Exception {
        Transaction transaction = new Transaction(
                from,
                null,
                null,
                null,
                to,
                value,
                null
        );
        return transaction;
    }


    public void importAccount(List<Map> list) {
        List accounts = redisTemplate.opsForList().range(LOCK_PLAT_USER, 0, redisTemplate.opsForList().size(LOCK_PLAT_USER));
        list.stream().forEach(map -> {
            String address = (String) map.get("address");
            if (null != accounts && accounts.contains(address)) {
                return;
            }
            redisTemplate.opsForList().leftPush(LOCK_PLAT_USER, address);
        });
    }

    public Long getAccountSize() {
        String key = LOCK_PLAT_USER;
        return redisTemplate.opsForList().size(key);
    }

    public List<Orders> getTransactionJson(String type) {
        final String startWith = getStartWith(type);
        String tempKey = LOCK_PLAT_TRANS_TEMP;
        String key = LOCK_PLAT_TRANS;
        List<Orders> result = new ArrayList<>();
        getTransactionsTemp(tempKey, key, result);
        List<com.mvc.ethereum.model.Transaction> transactionsTemp;
        java.util.function.Function<Orders, BigInteger> comparator = Orders::getNonce;
        Comparator<Orders> byNonce = Comparator.comparing(comparator);
        transactionsTemp = redisTemplate.opsForList().range(tempKey, 0, redisTemplate.opsForList().size(tempKey));
        redisTemplate.delete(tempKey);
        List<com.mvc.ethereum.model.Transaction> tempList = transactionsTemp.stream().filter(obj -> obj.getOrderId().indexOf(startWith) < 0).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(tempList)) {
            redisTemplate.opsForList().leftPushAll(tempKey, tempList);
        }
        result = result.stream().filter(obj -> obj.getOrderId().indexOf(startWith) >= 0).distinct().sorted(byNonce).collect(Collectors.toList());
        return result;
    }

    private void getTransactionsTemp(String tempKey, String key, List<Orders> result) {
        List<com.mvc.ethereum.model.Transaction> transactionsTemp = redisTemplate.opsForList().range(tempKey, 0, redisTemplate.opsForList().size(tempKey));
        if (null == transactionsTemp) {
            transactionsTemp = new ArrayList<>();
        }
        for (com.mvc.ethereum.model.Transaction transaction : transactionsTemp) {
            Orders orders = getOrders(transaction);
            if (orders.getValue().compareTo(BigDecimal.ZERO) > 0) {
                result.add(orders);
            }
        }
        while (redisTemplate.opsForList().size(key) > 0) {
            com.mvc.ethereum.model.Transaction transaction = (com.mvc.ethereum.model.Transaction) redisTemplate.opsForList().rightPop(key);
            redisTemplate.opsForList().leftPush(tempKey, transaction);
            Orders orders = getOrders(transaction);
            if (orders.getValue().compareTo(BigDecimal.ZERO) > 0) {
                result.add(orders);
            }
        }
        Collections.reverse(result);
        Map<String, Integer> nonceCount = new HashMap<>(result.size());
        for (int i = 0; i < result.size(); i++) {
            Orders orders = result.get(i);
            Integer nonceBase = nonceCount.get(orders.getFromAddress());
            nonceBase = null == nonceBase ? 0 : nonceBase;
            orders.setNonce(getNonce(orders.getFromAddress()).add(BigInteger.valueOf(nonceBase)));
            nonceCount.put(orders.getFromAddress(), nonceBase + 1);
        }
    }

    private String getStartWith(String type) {
        final String startWith;
        if ("all".equalsIgnoreCase(type)) {
            startWith = "";
        } else if ("transaction".equalsIgnoreCase(type)) {
            startWith = "T_T";
        } else {
            startWith = "T_C";
        }
        return startWith;
    }

    private Orders getOrders(com.mvc.ethereum.model.Transaction transaction) {
        Orders orders = new Orders();
        orders.setValue(Convert.fromWei(new BigDecimal(transaction.getActualQuantity()), Unit.ETHER));
        orders.setCreatedAt(transaction.getCreatedAt());
        orders.setUpdatedAt(transaction.getUpdatedAt());
        orders.setFromAddress(transaction.getFromAddress());
        orders.setToAddress(transaction.getToAddress());
        orders.setTokenType("MVC");
        if (!transaction.getOrderId().startsWith("LOCK_PLAT_T_")) {
            orders.setOrderId(String.format("LOCK_PLAT_T_%s", transaction.getOrderId()));
        } else {
            orders.setOrderId(transaction.getOrderId());
        }
        return orders;
    }

    public void importTransaction(List<Map> list) {
        redisTemplate.opsForList().rightPushAll(LOCK_PLAT_TRANS_LIST, list);
    }

    BigInteger getNonce(String address) {
        EthGetTransactionCount ethGetTransactionCount = null;
        try {
            ethGetTransactionCount = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).sendAsync().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return ethGetTransactionCount.getTransactionCount();
    }

    public void startTransaction() {
        try {
            Map<String, String> map = (Map<String, String>) redisTemplate.opsForList().rightPop(LOCK_PLAT_TRANS_LIST);
            if (null != map) {
                String orderId = map.get("orderId");
                String signature = map.get("signature");
                EthSendTransaction result = web3j.ethSendRawTransaction(signature).send();
                String tempOrderId = orderId.replaceAll("LOCK_PLAT_T_", "");
                if (tempOrderId.startsWith("T")) {
                    if (result.hasError()) {
                        log.warning(result.getError().getMessage());
                        transationService.updateStatusByOrderId(tempOrderId, 9);
                    } else {
                        transationService.updateHashByOrderId(tempOrderId, result.getTransactionHash());
                    }
                }
            }
            Thread.sleep(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
