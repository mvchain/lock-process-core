package com.mvc.user.config;

import com.mvc.auth.client.interceptor.ServiceAuthRestInterceptor;
import com.mvc.auth.client.interceptor.UserAuthRestInterceptor;
import com.mvc.common.handler.GlobalExceptionHandler;
import com.mvc.user.mapper.CoinInfoMapper;
import com.mvc.user.util.CoinUtil;
import feign.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author qyc
 */
@Configuration("admimWebConfig")
@Primary
public class WebConfiguration extends WebMvcConfigurerAdapter {
    @Bean
    GlobalExceptionHandler getGlobalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        ArrayList<String> commonPathPatterns = getExcludeCommonPathPatterns();
        registry.addInterceptor(getServiceAuthRestInterceptor()).addPathPatterns("/**").excludePathPatterns(commonPathPatterns.toArray(new String[]{}));
        registry.addInterceptor(getUserAuthRestInterceptor()).addPathPatterns("/**").excludePathPatterns(commonPathPatterns.toArray(new String[]{}));
        super.addInterceptors(registry);
    }

    @Bean
    ServiceAuthRestInterceptor getServiceAuthRestInterceptor() {
        return new ServiceAuthRestInterceptor();
    }

    @Bean
    UserAuthRestInterceptor getUserAuthRestInterceptor() {
        return new UserAuthRestInterceptor();
    }

    private ArrayList<String> getExcludeCommonPathPatterns() {
        ArrayList<String> list = new ArrayList<>();
        String[] urls = {
                "/v2/api-docs",
                "/swagger-resources/**",
                "/cache/**",
                "/api/log/save"
        };
        Collections.addAll(list, urls);
        return list;
    }

    @Bean
    public CoinUtil coinUtil(CoinInfoMapper coinInfoMapper) {
        coinInfoMapper.selectAll().stream().forEach(obj -> {
            CoinUtil.coinMap.put(obj.getId(), obj);
        });
        return new CoinUtil();
    }

    @Bean
    Request.Options feignOptions() {
        return new Request.Options(10 * 1000, 10 * 1000);
    }
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/static/cache/**").addResourceLocations(
//                "classpath:/META-INF/static/");
//        super.addResourceHandlers(registry);
//    }
}
