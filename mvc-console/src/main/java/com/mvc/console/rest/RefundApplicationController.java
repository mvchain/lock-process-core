package com.mvc.console.rest;

import com.mvc.common.context.BaseContextHandler;
import com.mvc.common.msg.Result;
import com.mvc.common.msg.ResultGenerator;
import com.mvc.common.rest.BaseController;
import com.mvc.console.service.RefundApplicationService;
import com.mvc.console.entity.RefundApplication;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

/**
 * ${DESCRIPTION}
 *
 * @author wanghaobin
 * @create 2017-06-08 11:51
 */
@RestController
@RequestMapping("refund")
public class RefundApplicationController extends BaseController<RefundApplicationService,RefundApplication> {


    @RequestMapping(value = "application", method = RequestMethod.POST)
    public @ResponseBody
    Result<String> getBalance(@RequestBody RefundApplication refundApplication) throws UnsupportedEncodingException {
        refundApplication.setUserId(new BigInteger(BaseContextHandler.getUserID()));
        refundApplication.setStatus(0);
        add(refundApplication);
        return ResultGenerator.genSuccessResult();
    }
}
