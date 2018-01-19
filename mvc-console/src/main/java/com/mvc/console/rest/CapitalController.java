package com.mvc.console.rest;

import com.mvc.common.rest.BaseController;
import com.mvc.console.service.CapitalService;
import com.mvc.console.entity.Capital;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ${DESCRIPTION}
 *
 * @author wanghaobin
 * @create 2017-06-08 11:51
 */
@RestController
@RequestMapping("capital")
public class CapitalController extends BaseController<CapitalService,Capital> {

}
