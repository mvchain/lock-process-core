package com.mvc.console.rest;

import com.mvc.common.msg.Result;
import com.mvc.common.msg.ResultGenerator;
import com.mvc.common.rest.BaseController;
import com.mvc.console.service.ConfigService;
import com.mvc.console.entity.CoinInfo;
import com.mvc.console.entity.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * config controller
 *
 * @author qiyichen
 * @create 2018/1/10 14:07
 */
@Controller
@RequestMapping("config")
public class ConfigController extends BaseController<ConfigService,CoinInfo> {

    @Autowired
    private ConfigService configService;

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public @ResponseBody Result insert(@RequestBody Config config){
        configService.insertOrUpdate(config);
        return ResultGenerator.genSuccessResult("ok");
    }

}
