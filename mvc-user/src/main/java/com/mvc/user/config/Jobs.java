package com.mvc.user.config;

import com.mvc.user.service.UserService;
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
    private UserService userService;

    /**
     * update user if the eth address is null
     */
    @Scheduled(cron = "*/10 * * * * ?")
    public void updateAddress() {
        userService.updateAddress();
    }
}