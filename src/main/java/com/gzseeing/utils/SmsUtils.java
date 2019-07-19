package com.gzseeing.utils;

import java.util.Date;
import java.text.SimpleDateFormat;
import com.taobao.api.TaobaoClient;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumQueryRequest;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumQueryResponse;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

public class SmsUtils {
	
	//private static final String apikey = "74c9a07a5f9996ced7157239b95d72c0";
	
	private static final String appkey = "23865515";
	private static final String secret = "ab0f99f0524e32ca21d7ce200888fafa";
	
	/**
	 * @author joyol
	 * @Description 
	 * @param mobile		手机号码
	 * @param freeSignName	签名
	 * @param templateCode	短信模板
	 * @param vcode			验证码
	 * @param product		系统模板的第二个参数
	 * @return
	 */
	public static String verificationCode(String mobile,String freeSignName,String templateCode,String vcode, String product) {
		TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", appkey, secret);
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setExtend("123456");//固定
		req.setSmsType("normal");//固定
		req.setSmsFreeSignName(freeSignName);
		if (product == null) {
			req.setSmsParamString("{\"code\":\""+vcode+"\"}");  //参数
		}else {
			req.setSmsParamString("{\"code\":\""+vcode+"\",\"product\":\""+product+"\"}");  //参数
		}
		req.setRecNum(mobile);
		req.setSmsTemplateCode(templateCode);
		String body = "";
		try {
			AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
			body = rsp.getBody();
			return body;
		} catch (Exception e) {
			LogUtils.error(LogUtils.getStackTrace(e));
		}
		return "bodyError";
	}
	
	public static String checkSuccess(String bizId,String recNum,Date date){
		TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", appkey, secret);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String queryDate = sdf.format(date);
		AlibabaAliqinFcSmsNumQueryRequest req = new AlibabaAliqinFcSmsNumQueryRequest();
		req.setBizId(bizId);
		req.setRecNum(recNum);
		req.setQueryDate(queryDate);
		req.setCurrentPage(1l);
		req.setPageSize(5l);
		String body = "";
		try {
			AlibabaAliqinFcSmsNumQueryResponse rsp = client.execute(req);
			body = rsp.getBody();
			return body;
		} catch (Exception e) {
			LogUtils.error(LogUtils.getStackTrace(e));
		}
		return "bodyError";
	}
	
	/**
	 * http://www.dingdongdx.com
	 * @author joyol
	 * @Description 
	 * @param photoNumber
	 * @param code
	 */
	@Deprecated
	public static void test(String photoNumber,String code) {
		StringBuffer param = new StringBuffer();
		param.append("apikey=");
		//param.append(apikey);
		param.append("&mobile=");
		param.append(photoNumber);
		param.append("&content=");
		param.append("【joyol】尊敬的用户，你的验证码是："+code+"，请在10分钟内输入。请勿告诉其他人。");
		HttpRequest.sendPost("https://api.dingdongcloud.com/v1/sms/sendyzm", param.toString());
	}
	
}
