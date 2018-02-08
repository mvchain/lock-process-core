package com.mvc.common.web;

import com.alibaba.fastjson.JSON;
import com.mvc.common.msg.Result;
import com.mvc.common.msg.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author qyc
 */
@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    private static final String CONTENT_TYPE_KEY = "Content-type";
    private static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * 统一异常处理
     *
     * @param exceptionResolvers 异常处理器列表
     */
    @Override
    public void configureHandlerExceptionResolvers(
            List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add(new HandlerExceptionResolver() {
            @SuppressWarnings("unchecked")
            @Override
            public ModelAndView resolveException(HttpServletRequest request,
                                                 HttpServletResponse response, Object handler, Exception e) {
                Result result = new Result();
                if (e instanceof NoHandlerFoundException) {
                    result.setCode(ResultCode.NOT_FOUND)
                            .setMessage("接口 [" + request.getRequestURI() + "] 不存在");
                } else if (e instanceof ServletException) {
                    result.setCode(ResultCode.FAILURE).setMessage(e.getMessage());
                } else if (e instanceof BindException || e instanceof MethodArgumentNotValidException) {
                    Map<String, String> fieldErrors = getFieldErrors(e);
                    result.setCode(ResultCode.VALIDATE_ERROR).setMessage("请求参数校验不通过").setData(fieldErrors);
                } else {
                    result.setCode(ResultCode.INTERNAL_SERVER_ERROR)
                            .setMessage("接口 [" + request.getRequestURI() + "] 内部错误，请检查");
                    String message;
                    if (handler instanceof HandlerMethod) {
                        HandlerMethod handlerMethod = (HandlerMethod) handler;
                        message = String.format("接口 [%s] 出现异常，方法：%s.%s，异常摘要：%s",
                                request.getRequestURI(),
                                handlerMethod.getBean().getClass().getName(),
                                handlerMethod.getMethod().getName(),
                                e.getMessage());
                    } else {
                        message = e.getMessage();
                    }
                    log.error(message, e);
                }
                responseResult(response, result);
                return new ModelAndView();
            }
        });
    }

    /**
     * 解决跨域问题,如果需要可以将注释打开
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // registry.addMapping("/**")
        //     .allowedOrigins("*")
        //     .allowedMethods("GET", "HEAD", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "TRACE");
    }

    /**
     * 不加以下代码会出现swagger-ui.html 404
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    private Map<String, String> getFieldErrors(Exception e) {
        List<FieldError> fieldErrors =
                e instanceof BindException ? ((BindException) e).getFieldErrors()
                        : ((MethodArgumentNotValidException) e).getBindingResult().getFieldErrors();
        // 支持在资源文件中定义错误信息，错误信息的key作为验证注解的message属性值，用大括号括起来
        return fieldErrors.stream().collect(Collectors.toMap(fieldError -> fieldError.getField(),
                fieldError -> fieldError.getDefaultMessage()));
    }

    private void responseResult(HttpServletResponse response, Result result) {
        response.setCharacterEncoding(DEFAULT_CHARSET);
        response.setHeader(CONTENT_TYPE_KEY, MediaType.APPLICATION_JSON_UTF8_VALUE.intern());
        response.setStatus(HttpStatus.OK.value());
        try (PrintWriter pw = response.getWriter();) {
            pw.write(JSON.toJSONString(result));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
