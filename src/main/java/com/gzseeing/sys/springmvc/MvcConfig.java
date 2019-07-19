package com.gzseeing.sys.springmvc;

import java.util.List;
import org.springframework.context.annotation.Bean;
import com.gzseeing.sys.springmvc.convert.DateConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;

/**
 * 
 * 注册拦截器,springmvc层面 Created by SYSTEM on 2017/8/16.
 */
@Configuration
public class MvcConfig extends AbstractAnnotationInterceptorMvcConfigurerAdapter {

	 /**
	  * url 匹配方式
	   * 1、 extends WebMvcConfigurationSupport
	   * 2、重写下面方法;
	   * setUseSuffixPatternMatch : 设置是否是后缀模式匹配，如“/user”是否匹配/user.*，默认真即匹配；
	   * setUseTrailingSlashMatch : 设置是否自动后缀路径模式匹配，如“/user”是否匹配“/user/”，默认真即匹配；//权限控制，存在隐患   漏洞访问方式： /user/del--> /test/user/del
	   */
	  @Override
	  public void configurePathMatch(PathMatchConfigurer configurer) {
	    configurer.setUseSuffixPatternMatch(false)
	          .setUseTrailingSlashMatch(false);
	  }
	  

	// 解决跨域问题
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**") // **代表所有路径
				.allowedOrigins("*") // allowOrigin指可以通过的ip，*代表所有，可以使用指定的ip，多个的话可以用逗号分隔，默认为*
				.allowedMethods("GET", "POST", "HEAD", "PUT", "DELETE") // 指请求方式
																		// 默认为*
				.allowCredentials(false) // 支持证书，默认为true
				.maxAge(3600) // 最大过期时间，默认为-1
				.allowedHeaders("*");
	}


	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		super.configureMessageConverters(converters);
		
	}


	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		super.extendMessageConverters(converters);
	}
	
//	@Bean
//	public  FormattingConversionService conversionService(DateConverter dateConverter){
//		 FormattingConversionServiceFactoryBean fb = new FormattingConversionServiceFactoryBean();
//		 FormattingConversionService formattingConversionService = fb.getObject();
//		 return formattingConversionService;
//	}
	
	
//	 @Override
//	 public void addResourceHandlers(ResourceHandlerRegistry registry) {
//	 //可以是file:E:/
//	 //默认的 /public /static /resources，/META-INF/resourc
//	// registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
//	 //注意最后有个 /斜杠！！！！
//	// registry.addResourceHandler("/image/**").addResourceLocations("classpath:/image/");
//	 super.addResourceHandlers(registry);
//	 }

}
