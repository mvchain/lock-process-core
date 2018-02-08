package com.mvc.console;

import com.ace.cache.EnableAceCache;
import com.mvc.auth.client.EnableAceAuthClient;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author qyc
 */
@EnableEurekaClient
@EnableCircuitBreaker
@SpringBootApplication
@EnableFeignClients({"com.mvc.console.rpc.service", "com.mvc.auth.client.feign"})
@EnableScheduling
@EnableAceAuthClient
@ServletComponentScan({"com.mvc.user.config.druid"})
@EnableAceCache
@EnableTransactionManagement
public class ConsoleBootstrap {
    public static void main(String[] args) {
        new SpringApplicationBuilder(ConsoleBootstrap.class).web(true).run(args);
    }
}
