package com.mvc.ethereum;

import com.ace.cache.EnableAceCache;
import com.mvc.auth.client.EnableAceAuthClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author qyc
 */
@EnableEurekaClient
@EnableCircuitBreaker
@SpringBootApplication
@EnableFeignClients({"com.mvc.auth.client.feign"})
@EnableScheduling
@EnableAceAuthClient
@ServletComponentScan("com.mvc.user.config.druid")
@EnableAceCache
@EnableTransactionManagement
@EnableAsync
public class EthereumApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(EthereumApplication.class, args);
    }

}