package com.mvc.auth.client.configuration;

import com.mvc.auth.client.config.ServiceAuthConfig;
import com.mvc.auth.client.config.UserAuthConfig;
import org.springframework.cloud.bus.jackson.RemoteApplicationEventScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author qyc
 */
@Configuration
@ComponentScan({"com.mvc.auth.client", "com.mvc.auth.common.event"})
@RemoteApplicationEventScan(basePackages = "com.mvc.auth.common.event")
public class AutoConfiguration {
    @Bean
    ServiceAuthConfig getServiceAuthConfig() {
        return new ServiceAuthConfig();
    }

    @Bean
    UserAuthConfig getUserAuthConfig() {
        return new UserAuthConfig();
    }
}
