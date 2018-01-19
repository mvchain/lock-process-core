package com.mvc.console.rest;

import com.mvc.common.rest.BaseController;
import com.mvc.console.service.CoinInfoService;
import com.mvc.console.entity.CoinInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ${DESCRIPTION}
 *
 * @author wanghaobin
 * @create 2017-06-08 11:51
 */
@RestController
@RequestMapping("coinInfo")
public class CoinInfoController extends BaseController<CoinInfoService,CoinInfo> {

}
