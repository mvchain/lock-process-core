package com.mvc.ethereum.configuration;

import com.alibaba.fastjson.JSON;
import com.mvc.ethereum.service.TransationService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.quorum.Quorum;
import rx.Observable;
import rx.Subscription;

/**
 * @author qyc
 */
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

    @Override
    @Async
    public void run(String... args) throws InterruptedException {
        Subscription sub = null;
        EthFilter filter = new EthFilter(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST, walletConfig.getAddressList());
        // 目前只监听代币信息
        log.info("transcation start listen");
        Observable<org.web3j.protocol.core.methods.response.Log> ob = web3j.ethLogObservable(filter);
        while (true) {
            Thread.sleep(1);
            if (null == sub || sub.isUnsubscribed()) {
                sub = getSubscription(ob);
            }
        }
    }

    private Subscription getSubscription(Observable<org.web3j.protocol.core.methods.response.Log> ob) {
        Subscription sub = null;
        try {
            sub = ob.subscribe(tx -> {
                log.info(JSON.toJSONString("received tx:" + JSON.toJSONString(tx)));
                transationService.update(tx);
            });
        } catch (Exception e) {
            ob.retry();
            e.printStackTrace();
            log.warning(e.getMessage());
        }
        return sub;
    }

}