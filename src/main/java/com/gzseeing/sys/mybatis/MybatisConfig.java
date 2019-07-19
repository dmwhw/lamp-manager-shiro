package com.gzseeing.sys.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@MapperScan("com.gzseeing.*.mapper*")
@EnableTransactionManagement(order=2) //启用事务  //order：1、Service 2、Transaction
//默认配置下 Spring 只会回滚运行时、未检查异常（继承自 RuntimeException 的异常）或者 Error。参考这里
//@Transactional 注解只能应用到 public 方法才有效。
public class MybatisConfig {
	
	   /*
	    * 分页插件，自动识别数据库类型
	    * 多租户，请参考官网【插件扩展】
	    */
	   @Bean
	   public PaginationInterceptor paginationInterceptor() {
	      return new PaginationInterceptor();
	   }

//	   @Bean
//	   @Scope("prototype")
//	   public MybatisSqlSessionTemplate mybatisSqlSessionTemplate(SqlSessionFactory sqlSessionFactory){
//		   return new MybatisSqlSessionTemplate(sqlSessionFactory);
//	   }
	   
	   /*
	    * oracle数据库配置JdbcTypeForNull
	    * 参考：https://gitee.com/baomidou/mybatisplus-boot-starter/issues/IHS8X
	    不需要这样配置了，参考 yml:
	    mybatis-plus:
	      confuguration
	        dbc-type-for-null: 'null' *//*
	   @Bean
	   public ConfigurationCustomizer configurationCustomizer(){
	       return new MybatisPlusCustomizers();
	   }

	   class MybatisPlusCustomizers implements ConfigurationCustomizer {

	       @Override
	       public void customize(org.apache.ibatis.session.Configuration configuration) {
	           configuration.setJdbcTypeForNull(JdbcType.NULL);
	       }
	   }*/
	   
}
