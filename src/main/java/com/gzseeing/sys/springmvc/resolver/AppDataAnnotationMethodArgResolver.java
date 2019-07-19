package com.gzseeing.sys.springmvc.resolver;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.gzseeing.sys.model.AppData;
import com.gzseeing.sys.model.exception.MsgException;
import com.gzseeing.sys.springmvc.annotation.Appdata;
import com.gzseeing.sys.springmvc.annotation.SpringResolver;
import com.gzseeing.utils.GsonUtils;
import com.gzseeing.utils.LogUtils;
@SpringResolver
public class AppDataAnnotationMethodArgResolver implements HandlerMethodArgumentResolver{

	//TODO
	//private static ThreadLocal<AppData> appdata=new ThreadLocal<AppData>();//线程复用导致出现问题
	
	private final static String APP_DATA="APP_DATA";
	@Override
	public Object resolveArgument(MethodParameter arg0, ModelAndViewContainer arg1, NativeWebRequest arg2,
			WebDataBinderFactory arg3) throws Exception {
		HttpServletRequest req=(HttpServletRequest)arg2.getNativeRequest();
		String APPDATA_KEY=Thread.currentThread().getName()+APP_DATA;
		Appdata appdataAnnotation = arg0.getParameterAnnotation(Appdata.class);
		String app_data=req.getParameter(appdataAnnotation.parentName());
		if (app_data==null||"".equals(app_data)){
			return null;
		}
		AppData app =(AppData) req.getAttribute(APPDATA_KEY);
		if (app==null){
			LogUtils.debug("init appdata for this request...");
			try {
				app = AppData.newInstance().setAppData(GsonUtils.toMap(app_data));
				req.setAttribute(APPDATA_KEY, app);
			} catch (Exception e) {
 				//e.printStackTrace();
				throw new MsgException(90006, "数据格式错误");
			}
		}
		//getdata
		String key=appdataAnnotation.value();
		if (key==null||"".equals(key)){
			key=arg0.getParameterName();
		}
		Object data = app.getData(key);
		//cast
		if (data==null){
			return null;
		}
		if (data.getClass().equals(arg0.getParameterType())) return data;//no need to convert
		if (arg0.getParameterType().equals(Integer.class)){
			return app.getDataInteger(key);
		}
		if (arg0.getParameterType().equals(Double.class)){
			return  app.getDataDouble(key);
		}
		return data;
	}

	@Override
	public boolean supportsParameter(MethodParameter arg0) {
        return arg0.hasParameterAnnotation(com.gzseeing.sys.springmvc.annotation.Appdata.class);
	}

}
