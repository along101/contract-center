package com.along101.contract.config;

import com.along101.contract.interceptor.PAuthInterceptor;
import com.along101.contract.interceptor.VersionInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author zhangchengxi
 * Date 2018/6/11
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Bean
    public PAuthInterceptor getPAuthInterceptor(){
        return new PAuthInterceptor();
    }

    @Bean
    public VersionInterceptor getVersionInterceptor(){
        return new VersionInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getPAuthInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(getVersionInterceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}
