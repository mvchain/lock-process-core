package com.mvc.ethereum.configuration;

import com.mvc.ethereum.service.RpcService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author qyc
 */
@Component
@Order(value = 1)
@Log
public class TransactionStartupRunner implements CommandLineRunner {

    @Autowired
    RpcService rpcService;

    @Override
    @Async
    public void run(String... args) throws InterruptedException {
        while (true) {
            rpcService.startTransaction();
            Thread.sleep(1);
        }
    }

}