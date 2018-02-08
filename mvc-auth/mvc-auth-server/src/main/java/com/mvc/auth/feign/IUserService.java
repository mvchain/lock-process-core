package com.mvc.auth.feign;

import com.mvc.api.vo.user.UserInfo;
import com.mvc.auth.configuration.FeignConfiguration;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * ${DESCRIPTION}
 *
 * @author wanghaobin
 * @create 2017-06-21 8:11
 */
@FeignClient(value = "mvc-admin", configuration = FeignConfiguration.class)
public interface IUserService {
    /**
     * validate
     *
     * @param username
     * @param password
     * @return
     */
    @RequestMapping(value = "/api/user/validate", method = RequestMethod.POST)
    public UserInfo validate(@RequestParam("username") String username, @RequestParam("password") String password);
}
