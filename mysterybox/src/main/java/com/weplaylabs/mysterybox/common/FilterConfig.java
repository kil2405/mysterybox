package com.weplaylabs.mysterybox.common;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig
{
    public FilterRegistrationBean<CustomFilter> getFilterRegistrationBean() {
        FilterRegistrationBean<CustomFilter> registrationBean = new FilterRegistrationBean<>(new CustomFilter());
		registrationBean.addUrlPatterns("/mysterybox/api/*"); // 서블릿 등록 빈 처럼 패턴을 지정해 줄 수 있다.
		return registrationBean;
    }
}
