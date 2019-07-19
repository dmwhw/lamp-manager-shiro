package com.gzseeing.sys.springmvc.resolver;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.gzseeing.sys.model.AppData;
import com.gzseeing.sys.springmvc.annotation.SpringResolver;
import com.gzseeing.utils.GsonUtils;
@SpringResolver
public class AppDataMethodArgResolver implements HandlerMethodArgumentResolver{

	@Override
	public Object resolveArgument(MethodParameter arg0, ModelAndViewContainer arg1, NativeWebRequest arg2,
			WebDataBinderFactory arg3) throws Exception {
		String app_data=null;
		System.out.println("********************YYYYYY********************************");
		Iterator<String> parameterNames = arg2.getParameterNames();
		while(parameterNames.hasNext()){
			System.out.println("000000"+arg2.getParameter(parameterNames.next()));
		}
		if ((app_data=((HttpServletRequest)arg2.getNativeRequest()).getParameter("app_data"))!=null){
			try {
				return AppData.newInstance().setAppData(GsonUtils.toMap(app_data));
			} catch (Exception e) {
 				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public boolean supportsParameter(MethodParameter arg0) {
		System.out.println("------ susussu----"+arg0.getParameterType().equals(AppData.class));
        return arg0.getParameterType().equals(AppData.class);
	}

}
