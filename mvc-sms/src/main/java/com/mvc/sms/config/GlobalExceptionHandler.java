package com.mvc.sms.config;

import com.mvc.common.constant.CommonConstants;
import com.mvc.common.exception.BaseException;
import com.mvc.common.msg.BaseResponse;
import com.mvc.common.msg.Result;
import com.mvc.common.msg.ResultGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by ace on 2017/9/8.
 */
@ControllerAdvice("com.mvc")
@ResponseBody
public class GlobalExceptionHandler {

    private static final String CONTENT_TYPE_KEY = "Content-type";
    private static final String DEFAULT_CHARSET = "UTF-8";

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(BaseException.class)
    public BaseResponse baseExceptionHandler(HttpServletResponse response, BaseException ex) {
        logger.error(ex.getMessage(),ex);
        response.setStatus(500);
        return new BaseResponse(ex.getStatus(), ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public BaseResponse otherExceptionHandler(HttpServletResponse response, Exception ex) {
        response.setStatus(500);
        logger.error(ex.getMessage(),ex);
        return new BaseResponse(CommonConstants.EX_OTHER_CODE, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result methodArgumentNotValidException(HttpServletResponse response, MethodArgumentNotValidException ex) {
        response.setStatus(400);
        return ResultGenerator.genFailResult("", ex.getBindingResult().getAllErrors());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Result methodIllegalArgumentException(HttpServletResponse response, IllegalArgumentException ex) {
        response.setStatus(400);
        return ResultGenerator.genFailResult(ex.getMessage());
    }

}
