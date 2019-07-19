package com.gzseeing.sys.springmvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface SpringHandlerInterceptor {
    String[] value() default {};
 
    String[] includePatterns() default {};
 
    String[] excludePatterns() default {};
 
    int order() default Ordered.LOWEST_PRECEDENCE;
}