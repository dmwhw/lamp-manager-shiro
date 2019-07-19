package com.gzseeing.sys.springmvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Auth {
	String requireUser();//需要登陆的用户
	String[] requireRoles();//需要的角色
	boolean isAuth() default false;//是否需要授权访问
}
