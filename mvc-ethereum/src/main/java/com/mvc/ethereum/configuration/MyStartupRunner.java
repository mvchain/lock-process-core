package com.mvc.ethereum.configuration;

import com.mvc.ethereum.service.TransationService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.quorum.Quorum;
import rx.Observable;
import rx.Subscription;

@Component
@Order(value = 1)
@Log
public class MyStartupRunner implements CommandLineRunner {

    @Autowired
    private Web3j web3j;
    @Autowired
    Quorum quorum;

    @Autowired
    private TransationService transationService;
    @Autowired
    private WalletConfig walletConfig;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    @Async
    public void run(String... args) {
        Subscription sub = null;
        Subscription sub2 = null;
//        sub2= getSubscription2();
//        initContract();

        EthFilter filter = new EthFilter(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST, walletConfig.getAddressList());
        // 目前只监听代币信息
        Observable<org.web3j.protocol.core.methods.response.Log> ob = web3j.ethLogObservable(filter);
        while (true) {
            if (null == sub || sub.isUnsubscribed()) {
                sub = getSubscription(ob);
            }
//            if (null == sub2 || sub2.isUnsubscribed()) {
//                sub = getSubscription();
//            }
        }


    }

    private void initContract() throws Exception {
//
//
//        BigInteger aliceQty = BigInteger.valueOf(1_000_000);
//        Credentials credentials= Credentials.create("7ef5eebbadeca3ed3640512cda006ea332a8e6c0097d32e8b653f768452ac9ed");
//        RemoteCall<HumanStandardToken> call = HumanStandardToken.deploy(web3j, credentials, Contract.GAS_PRICE, Contract.GAS_LIMIT, aliceQty, "testHumen", BigInteger.valueOf(18), "w3j$");
//        HumanStandardToken result = call.send();
//        System.out.println(JSON.toJSONString(result));
    }

    private Subscription getSubscription(Observable<org.web3j.protocol.core.methods.response.Log> ob) {

        Subscription sub = null;
        try {
            sub = ob.subscribe(tx -> {
                transationService.update(tx);
            });
        } catch (Exception e) {
            ob.retry();
            log.throwing(e.getClass().getName(), e.getMessage(), e);
        }
        return sub;
    }

    private Subscription getSubscription2() {
        Subscription sub = web3j.pendingTransactionObservable().subscribe(tx -> {
            if (walletConfig.isWalletAccount(tx.getFrom())) {
                // 这里需要合约修改后修改
            }
        });
        return sub;
    }


}