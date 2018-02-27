package com.mvc.console.config;

import com.mvc.console.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author qyc
 * transfer token to the super account
 */
@Component
public class Jobs {

    @Autowired
    private ConfigService configService;
    /**
     * update config
     */
    @Scheduled(cron = "*/10 * * * * ?")
    public void updateConfig() {
        configService.updateConfig();
    }

}