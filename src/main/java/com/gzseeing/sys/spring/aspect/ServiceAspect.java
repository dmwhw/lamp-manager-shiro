package com.gzseeing.sys.spring.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.gzseeing.sys.model.exception.AppErrException;
import com.gzseeing.sys.model.exception.MsgException;
import com.gzseeing.utils.LogUtils;

@Aspect
@Component
@Order(1)//order：1、Service 2、Transaction
public class ServiceAspect {
    @Around("execution(* com.gzseeing.*.service.impl..*.*(..))")
    public Object aroundInvoke(ProceedingJoinPoint jp) 
            throws Throwable {
		String MethodName=jp.getSignature().getDeclaringType().getSimpleName()+"."+jp.getSignature().getName();
	       LogUtils.info("【访问Service】"+MethodName);
	        try {
	        	//long start=System.currentTimeMillis();
	            Object obj=jp.proceed();
		 	    //LogUtils.info(MethodName+" end, time Used-->"+(System.currentTimeMillis()-start)+"ms");
		 	    return obj;
	        }catch(MsgException e){
	        	LogUtils.info(e.getMsg());
	        	//自定义信息提示异常,不记录错误栈信息，只显示提示信息消息
	        	throw e;
	        }catch (AppErrException e) {
	        	//违规操作导致的异常，记录日志
			    LogUtils.error(MethodName+" has encountered illegal operation");
		        LogUtils.error(LogUtils.getStackTrace(e));
	        	throw e;
			}
	        catch (Throwable e) {
	        	//未知错误
	        	//記錄日誌
		       LogUtils.error(MethodName+" has errors");
	           LogUtils.error(LogUtils.getStackTrace(e));
	           throw e;
	        }

    }

}