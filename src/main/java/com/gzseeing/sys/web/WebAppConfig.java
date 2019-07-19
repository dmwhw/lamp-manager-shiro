package com.gzseeing.sys.web;

import javax.servlet.Filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.gzseeing.sys.web.filter.MyFilter;

@ServletComponentScan//注册filter用的
@Configuration
public class WebAppConfig {
	
	@Bean
	public FilterRegistrationBean charSetFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new CharacterEncodingFilter());
		registration.addUrlPatterns("/*");
		registration.addInitParameter("encoding", "utf-8");
		registration.addInitParameter("forceEncoding", "true");
		registration.setName("charSetFilter");
		registration.setOrder(1);
		return registration;
	}
	@Bean
	public FilterRegistrationBean getMyFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(getMyFilter());
		registration.addUrlPatterns("/*");
		//registration.addInitParameter("paramName", "paramValue");
		registration.setName("myFilter");
		registration.setOrder(2);
		return registration;
	}

	@Bean
	public Filter getMyFilter() {
		return new MyFilter();
	}

}
