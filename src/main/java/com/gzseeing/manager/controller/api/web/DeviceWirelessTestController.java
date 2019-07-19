package com.gzseeing.manager.controller.api.web;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.locale.converters.DoubleLocaleConverter;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzseeing.manager.service.DeviceTestService;
import com.gzseeing.sys.model.R;
import com.gzseeing.sys.mymqtt.PubAcker;
import com.gzseeing.sys.mymqtt.VoicePublisherListener;
import com.gzseeing.sys.mymqtt.waiter.CaptureCountWaiter;
import com.gzseeing.sys.mymqtt.waiter.CaptureWaiter;
import com.gzseeing.sys.mymqtt.waiter.StringsContentWaiter;
import com.gzseeing.utils.Base64;
import com.gzseeing.utils.DateUtil;
import com.gzseeing.utils.FileUtils;
import com.gzseeing.utils.GsonUtils;
import com.gzseeing.utils.LampDataUtils;
import com.gzseeing.utils.LogUtils;
import com.gzseeing.utils.PathUtils;
import com.gzseeing.utils.ThreadUtils;
import com.gzseeing.utils.LampDataUtils.DataDivider;
import com.gzseeing.utils.LampDataUtils.HeaderFlag;
import com.haowen.mqtt.comp.MyMQTTClient;

 
/**
 * @author haowen
 * 设备的无线测试临时页
 */
@Controller
@RequestMapping("/api/web/device-test")
public class DeviceWirelessTestController {

	
	@Autowired
	private MyMQTTClient myMQTTClient;
	@Autowired
	private  DeviceTestService deviceTestService;
	@Autowired
	private VoicePublisherListener voicePublisherListener;
	/**
	 * 测试台灯的信号强度
	 * 
	 * 根据条件，归结为
	 * @author haowen
	 * @time 2019年3月7日上午10:22:22
	 * @Description  
	 * @return xhfw 信号范围 
	 * @return xhpjz信号平均值（string2位小数）
	 * @return xhcgcs 成功次数
	 */
	@RequestMapping("/rssi-test")
	@ResponseBody
	public R testLampWirelessRssi(String cstj,String deviceUuid){
		if(deviceUuid==null){
			return R.fail("40001","设备序号为空");
		}
		Boolean findIsTest = deviceTestService.findIsTest(deviceUuid);
		if (findIsTest==null){
			return R.fail("40001","设备不存在！");
		};		
		if (findIsTest){
			return R.fail("40001","设备已经测试！");

		};
		List<Double> rssiList=new ArrayList<>();
		double xhpjz=0d;
		String xhfw="NaN";
		for (int i=0;i<10;i++){
	 		String cmdReturn=deviceUuid+"/cmdReturn";;
	 		String cmd=deviceUuid+"/cmd";;
	 		String sender="13888888888";
			Map<String,Object> map=new HashMap<>();
			long time=System.currentTimeMillis()/1000L;
			map.put("cmd", "status");
			map.put("sender",sender);
			map.put("time", time);
			StringsContentWaiter stringsContentWaiter = new StringsContentWaiter(5000, cmdReturn, new String []{"cmd","status",""+time,sender } );
			//mqtt-core fix ;timeout无用
			MqttMessage msg = (MqttMessage) myMQTTClient.publishWithWaiter(cmd, 1, GsonUtils.parseJSON(map), 5000, cmdReturn, true, stringsContentWaiter);
			ThreadUtils.sleepOneSec();
			if (msg==null){
				continue;
			}
			byte[] payload = msg.getPayload();
			Map<String, Object> result= GsonUtils.toMap(new String( payload));
			Map<String, Object> data = (Map<String, Object>) result.get("data");
			rssiList.add((Double) data.get("rssi"));
		}
		if(!rssiList.isEmpty()){
			rssiList.sort((Double d1,Double d2)->d1.compareTo(d2));
			double sum=0d;
			for (Double double1 : rssiList) {
				sum+=double1;
			}
			xhpjz=new BigDecimal(sum/rssiList.size()).setScale(2, BigDecimal.ROUND_FLOOR).doubleValue();
			xhfw=rssiList.get(0)+"~"+rssiList.get(rssiList.size()-1);
		}
		//保存为数据
		StringBuffer content=
				new StringBuffer("【信号强度测试】：").append(DateUtil.dateFormat("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis())).append(":");
		content.append(" 信号范围：").append(xhfw).append(" ").append(" 信号平均值：").append(xhpjz).append("成功次数：").append(rssiList.size()).append("\n");
		content.append("raw data:").append(rssiList.toString()).append("\n\n\n");
		File targetFile=new File(PathUtils.getTomcatPath(),"/testlog/"+deviceUuid+cstj+".txt");
		FileUtils.appendToFile2(targetFile, content.toString());
		
		//返回数据
		return  R.ok().add("xhcgcs", rssiList.size()).add("xhfw",xhfw).add("xhpjz",xhpjz);
 	}
	/**
	 *  测试台灯的接收能力，通过发送语音实现。
	 * @author haowen
	 * @time 2019年3月7日上午10:22:14
	 * @Description 
	 * @return sbyszs台灯应该接收的包数量 
	 * @return  sbsjzs 实际的收包数
	 * @return  sbdszs 丢失总数
	 * @return sbdsl 丢失率
	 * @return sbzys总用时 
	 * 
	 */
	@RequestMapping("/recv-test")
	@ResponseBody
	public R testLampWirelessRecvAbility(String cstj,String deviceUuid){
		if(deviceUuid==null){
			return R.fail("40001","设备序号为空");
		}
		Boolean findIsTest = deviceTestService.findIsTest(deviceUuid);
		if (findIsTest==null){
			return R.fail("40001","设备不存在！");
		};		
		if (findIsTest){
			return R.fail("40001","设备已经测试！");

		};
		int sbyszs = 0;
		int sbsjzs =  0;
		int sbdszs = 0;
		double sbdsl =  0;
		int sbzys = -1;
		long startTime=0L;
		String cmdReturn=deviceUuid+"/cmdReturn";;
 		String cmd=deviceUuid+"/cmd";;
 		String voice=deviceUuid+"/voice";;
 		int requestTimeStamp=new Long( System.currentTimeMillis()/1000).intValue();
 		String sender= "13888888888";
		Map<String,Object> map=new HashMap<>();
		map.put("cmd", "voice");
		map.put("fileNum", 0);
		map.put("sender", sender);
		map.put("time",requestTimeStamp);
		
		MqttMessage returnResult = myMQTTClient.publishAndReturnResult(cmd, 1, GsonUtils.parseJSON(map), 5000, cmdReturn,true, "[\\S\\s]*((cmd)+).+((voiceRecv)+)[\\S\\s]*[\\S\\s]*");
		if (returnResult==null){
			return R.fail("40001","台灯无响应");
		}
		Map<String, Object> returnJson=GsonUtils.toMap( new String(  returnResult.getPayload()));
		if (!"10001".equals(  returnJson.get("msgCode"))){
			return R.fail("40001","设备正忙");
		}
		LogUtils.info("sendvoiceTomcatPath"+PathUtils.getTomcatPath());
		File file =new File(PathUtils.getTomcatPath(),"web/static/voice/testData.mp3");
		DataDivider divider = LampDataUtils.getDivider(HeaderFlag.FLAG_55AA55AA, LampDataUtils.CMD_VOICE, 512-16, requestTimeStamp, file, true);
		
		try {
			List<byte[]> data = divider.getData();
			sbyszs=data.size();
			startTime = System.currentTimeMillis(); 
			//发送第1~倒数第二个包
			for(int i=0;i<data.size()-1;i++){
				myMQTTClient.publishAsyn(voice,1,data.get(i));
				PubAcker acker=new PubAcker(voice,1000);
				voicePublisherListener.addAndWait(acker);
				ThreadUtils.sleep(30);//不能发送太快了 
			}
			//发送最后一个包，等待最后回复
			returnResult = myMQTTClient.publishAndReturnResult(voice, 1, data.get(sbyszs-1) , 3000, cmdReturn, true,  "[\\S\\s]*((cmd)+).+((voiceRecv)+)[\\S\\s]*");
			if (returnResult==null){
				return R.fail("40001","台灯无响应，本次发送的包个数："+data.size());
			}
			//处理结果
			returnJson=GsonUtils.toMap( new String(  returnResult.getPayload()));
			if ("10000".equals(  returnJson.get("msgCode"))|| "40000".equals(  returnJson.get("msgCode"))||"40001".equals(  returnJson.get("msgCode"))){
				sbzys = new Long(System.currentTimeMillis()-startTime).intValue();//仅供参考，实际会有延时不准确
				sbsjzs =  returnJson.get("totalRecv") ==null?0:( (Number)returnJson.get("totalRecv")).intValue();
				
				sbdszs=sbyszs-sbsjzs;
				sbdsl=new BigDecimal( sbdszs*100d/sbzys).setScale(2, BigDecimal.ROUND_FLOOR).doubleValue();

				//保存为数据
				StringBuffer content=
						new StringBuffer("【台灯收包能力测试】：").append(DateUtil.dateFormat("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis())).append(":");
				content
					.append(" 台灯应该接收的包数量 ：").append(sbyszs)
					.append(" 实际的收包数：").append(sbsjzs)
					.append(" 丢失总数：").append(sbdszs)
					.append(" 丢失率：").append(sbdsl +"%")
					.append(" 总用时 ：").append(sbzys +"ms")
					.append("\n");
				content.append("\n\n\n");
				File targetFile=new File(PathUtils.getTomcatPath(),"/testlog/"+deviceUuid+cstj+".txt");
				FileUtils.appendToFile(targetFile, content.toString());
			
				return R.ok("10001","")
						.add("sbyszs", sbyszs)
						.add("sbsjzs", sbsjzs)
						.add("sbdszs", sbdszs)
						.add("sbdsl", sbdsl)
						.add("sbzys", sbzys);
			}
			
			//保存为数据
//			StringBuffer content=
//					new StringBuffer(DateUtil.dateFormat("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis())).append(":");
//			content
//				.append(" 台灯应该接收的包数量 ：").append(sbyszs)
//				.append(" 实际的收包数：").append(sbsjzs)
//				.append(" 丢失总数：").append(sbdszs)
//				.append(" 丢失率：").append(sbdsl)
//				.append(" 总用时 ：").append(sbzys)
//				.append("\n");
//			content.append("\n\n\n");
//			File targetFile=new File(PathUtils.getTomcatPath(),"/testlog/"+deviceUuid+cstj+".txt");
//			FileUtils.appendToFile2(targetFile, content.toString());
//	
			//未有回复
			return R.fail("40001","播放失败，本次发送的包个数："+data.size());
			
		} catch (Exception e) {
 			e.printStackTrace();
		}
		return R.fail();

	}
	
	/**
	 * 测试台灯的发送能力
	 * @author haowen
	 * @time 2019年3月7日上午10:26:20
	 * @Description  
	 * @param testCondition
	 * @return fbyfzs 发包应发总数
	 * @return  fbsjzs 发包实际总数
	 * @return  fbdszs  发包丢失总数
	 * @return fbdsl 发包丢失率
	 * @return fbzys 发包总用时 
	 */
	@RequestMapping("/send-test")
	@ResponseBody
	public R testLampWirelessSendAbility(String cstj,String deviceUuid){
		if(deviceUuid==null){
			return R.fail("40001","设备序号为空");
		}
		Boolean findIsTest = deviceTestService.findIsTest(deviceUuid);
		if (findIsTest==null){
			return R.fail("40001","设备不存在！");
		};		
		if (findIsTest){
			return R.fail("40001","设备已经测试！");

		};
		int fbyfzs=0;
		int fbsjzs=0;
		int fbdszs=0;
		double fbdsl=0d;
		long fbzys=0;

 		String capturetopic=deviceUuid+"/capture";;
 		String cmd=deviceUuid+"/cmd";;
 		int requestTimeStamp=new Long( System.currentTimeMillis()/1000).intValue();
		Map<String,Object> map=new HashMap<>();
		map.put("cmd", "capture");
		map.put("sender", "13888888888");
		map.put("time", requestTimeStamp);
		CaptureCountWaiter waiter=new CaptureCountWaiter(requestTimeStamp, 5000);
		com.gzseeing.sys.mymqtt.waiter.CaptureCountWaiter.ProgresssPromoter progresssPromoter = waiter.getProgresssPromoter();
		Integer  recvSize=   (Integer) myMQTTClient.publishWithWaiter(cmd, 1, GsonUtils.parseJSON(map), 5000, capturetopic, true, waiter);
		
		if (recvSize==null||recvSize==0){
			return R.fail("40001","台灯无响应").add("img", "");
		}else{
			//保存为数据
			fbyfzs=progresssPromoter.getTotal();
			fbsjzs=progresssPromoter.getCurrent();
			fbdszs=fbyfzs-fbsjzs;
			fbdsl=new BigDecimal(fbdszs*1d/fbyfzs).setScale(2, BigDecimal.ROUND_FLOOR).doubleValue();
			fbzys=progresssPromoter.getTimeUsed();
			StringBuffer content=
					new StringBuffer("【台灯发包能力测试】：").append(DateUtil.dateFormat("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis())).append(":");
			content
				.append(" 发包应发总数 ：").append(fbyfzs)
				.append(" 发包实际总数：").append(fbsjzs)
				.append(" 发包丢失总数：").append(fbdszs)
				.append(" 发包丢失率：").append(fbdsl+"%")
				.append(" 发包总用时  ：").append(fbzys+"ms")
				.append("\n");
			content.append("\n\n\n");
			File targetFile=new File(PathUtils.getTomcatPath(),"/testlog/"+deviceUuid+cstj+".txt");
			FileUtils.appendToFile2(targetFile, content.toString());
	
			return R.ok()
					.add("fbyfzs", fbyfzs)
					.add("fbsjzs", fbsjzs)
					.add("fbdszs", fbdszs)
					.add("fbdsl", fbdsl)
					.add("fbzys", fbzys);

			
		}

	}
	
	
	@RequestMapping("/capture-success-count")
	@ResponseBody
	public R testCaptureSuccessCount(String cstj,String deviceUuid,String captureSuccessCount){
		StringBuffer content=
				new StringBuffer("【台灯截图测试】：").append(DateUtil.dateFormat("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis())).append(":");
		content
			.append(" 截图成功次数 ：").append(captureSuccessCount)
			.append("\n");
		content.append("\n\n\n");
		File targetFile=new File(PathUtils.getTomcatPath(),"/testlog/"+deviceUuid+cstj+".txt");
		FileUtils.appendToFile2(targetFile, content.toString());
		return R.ok();
	}
	

	
}
