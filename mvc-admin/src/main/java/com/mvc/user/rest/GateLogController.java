package com.mvc.user.rest;

import com.mvc.common.rest.BaseController;
import com.mvc.user.entity.GateLog;
import com.mvc.user.service.GateLogBiz;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author qyc
 * @create 2017-07-01 20:32
 */
@Controller
@RequestMapping("gateLog")
public class GateLogController extends BaseController<GateLogBiz, GateLog> {
}
