package com.gzseeing.manager.controller.api.web;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.gzseeing.manager.entity.DeviceTestSubject;
import com.gzseeing.manager.service.DeviceTestSubjectService;
import com.gzseeing.sys.mymqtt.PubAcker;
import com.gzseeing.sys.mymqtt.VoicePublisherListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzseeing.manager.entity.DeviceTest;
import com.gzseeing.manager.service.DeviceTestService;
import com.gzseeing.sys.model.PageBean;
import com.gzseeing.sys.model.R;
import com.gzseeing.sys.mymqtt.waiter.CaptureWaiter;
import com.gzseeing.sys.mymqtt.waiter.CaptureWaiter.ProgresssPromoter;
import com.gzseeing.utils.Base64;
import com.gzseeing.utils.DateUtil;
import com.gzseeing.utils.GsonUtils;
import com.gzseeing.utils.LampDataUtils;
import com.gzseeing.utils.LampDataUtils.DataDivider;
import com.gzseeing.utils.LampDataUtils.HeaderFlag;
import com.gzseeing.utils.LogUtils;
import com.gzseeing.utils.PathUtils;
import com.gzseeing.utils.ThreadUtils;
import com.gzseeing.utils.View;
import com.haowen.mqtt.comp.MyMQTTClient;

/**
 * @author haowen
 *
 */
@Controller
@RequestMapping("/api/web/device-test")
public class DeviceTestController {

	
	
	@Autowired
	private DeviceTestService deviceTestService;
	@Autowired
	private DeviceTestSubjectService deviceTestSubjectService;
	
	@Autowired
	private MyMQTTClient myMQTTClient;
	
	@RequestMapping("/pagebean")
	@ResponseBody
	public R ajaxGetDeviceTestPageBean(
			String deviceNickName, 
			Integer isOnLine,
			@DateTimeFormat(pattern="yyyy-MM-dd") Date  start,
			@DateTimeFormat(pattern="yyyy-MM-dd") Date end,
			String mac, 
			String snno, 
			String remark, 
			Integer status, 
			@RequestParam(defaultValue="1",name="page") Integer pageIndex,
			@RequestParam(defaultValue="10",name="rows") Integer pageSize){
		start = DateUtil.setDate(start, null, null, null, 0, 0, 0,0);
		end = DateUtil.setDate(end, null, null, null, 23, 59, 59,999);
		if(start!=null&&end==null){
			R r=R.fail("40001","结束日期不能为空");
			r.put("rows",new ArrayList<>());
			r.put("total",0);;
			return r;
		}
		if(start==null&&end!=null){
			R r=R.fail("40001","开始日期不能为空");
			r.put("rows",new ArrayList<>());
			r.put("total",0);;
			return r;
		}
		PageBean<DeviceTest> deviceTestByPage = deviceTestService.getDeviceTestByPage(deviceNickName, isOnLine, start, end, mac, snno, remark, status, pageIndex, pageSize);
		 R r = R.ok().add("pageBean", deviceTestByPage);
		 r.put("rows",deviceTestByPage.getList() );
		r.put("total",deviceTestByPage.getTotalCount());;
		return r;
	}
	
	
	
	
	@RequestMapping("/export")
	public void ajaxGetDeviceTestExport(
			String deviceNickName, 
			Integer isOnLine,
			@DateTimeFormat(pattern="yyyy-MM-dd") Date  start,
			@DateTimeFormat(pattern="yyyy-MM-dd") Date end,
			String mac, 
			String snno, 
			String remark, 
			Integer status,
			HttpServletResponse res
			){
		start = DateUtil.setDate(start, null, null, null, 0, 0, 0,0);
		end = DateUtil.setDate(end, null, null, null, 23, 59, 59,999);
		if(start!=null&&end==null){
			View.returnBytes("end date can not be null".getBytes(), res);
		}
		if(start==null&&end!=null){
			View.returnBytes("start date can not be null".getBytes(), res);
		}
		File deviceTestExportFile = deviceTestService.getDeviceTestExportFile(deviceNickName, isOnLine, start, end, mac, snno, remark, status );
		View.returnFile(deviceTestExportFile,deviceTestExportFile.getName() ,res);
	}
	
	
	
	@RequestMapping("/rename")
	@ResponseBody
	public R reNameDevice(Integer id, @RequestParam(defaultValue="")String newName){
		if (id==null){
			return  R.fail("40001","id不能为空");
		}
		deviceTestService.updateDeviceTestName(id, newName);
		return  R.ok();
	}
	
	@RequestMapping("/confirm-test-done")
	@ResponseBody
	public R reNameDevice( @RequestParam("ids[]")Integer ids []){
		if (ids==null){
			return  R.fail("40001","id不能为空");
		}
		deviceTestService.confirmDone(  ids);
 		return  R.ok();
	}
	
	
	@RequestMapping("/changremark")
	@ResponseBody
	public R changeRemark(Integer id,@RequestParam(defaultValue="")String remark){
		if (id==null){
			return  R.fail("40001","id不能为空");
		}

		deviceTestService.changRemark(id, remark);
		return  R.ok();
	}


    /**
     * 获取测试结果
     * @param deviceIds
     * @return
     */
	@ResponseBody
	@RequestMapping("/test-subject-result")
	public R ajaxGetDeviceTestSubjectTestResult(@RequestParam(required = false,value = "deviceIds[]") Integer []deviceIds){
        Map<Integer, List<DeviceTestSubject>> testResult = deviceTestSubjectService.getDeviceTestResultByDeviceIds(deviceIds);
        return R.ok().add("result",testResult);
	}

	/**
	 *
	 * 标记测试结果
	 * @param deviceId
	 * @param remark
	 * @param status
	 * @param subjectCode
	 * @return
	 */
	@RequestMapping("/test-result")
	@ResponseBody
	public R addTestResult(Integer deviceId,String remark,Integer status,String subjectCode){
		DeviceTestSubject dts=new DeviceTestSubject();
		dts.setDeviceId(deviceId);
		dts.setSubjectCode(subjectCode);
		dts.setStatus(status);
		dts.setRemark(remark);
		dts.setLastTestTime(DateUtil.getNowDate());
		deviceTestSubjectService.saveOrUpdateBySubjectCodeAndDeviceId(dts);
		return R.ok();
	}
	
	@RequestMapping("/status")
	@ResponseBody
	public R ajaxGetStatus(String deviceUuid ){
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
 		String cmdReturn=deviceUuid+"/cmdReturn";;
 		String cmd=deviceUuid+"/cmd";;
		Map<String,Object> map=new HashMap<>();
		map.put("cmd", "status");
		map.put("sender", "13888888888");
		map.put("time", System.currentTimeMillis()/1000L);
		MqttMessage returnResult = myMQTTClient.publishAndReturnResult(cmd, 1, GsonUtils.parseJSON(map), 3500, cmdReturn,true, "[\\S\\s]*((cmd)+).+((status)+)[\\S\\s]*");
		if (returnResult==null){
			return R.fail("40001","台灯无响应-未返回status");
		}
		return R.ok().add("status", new String(returnResult.getPayload()));
	}
	
	@RequestMapping("/adjust-voice")
	@ResponseBody
	public R adjustVolume(Integer voiceLevel,String deviceUuid){
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
 		String cmdReturn=deviceUuid+"/cmdReturn";;
 		String cmd=deviceUuid+"/cmd";;
		Map<String,Object> map=new HashMap<>();
		map.put("cmd", "status");
		map.put("sender", "13888888888");
		map.put("time", System.currentTimeMillis()/1000L);
		MqttMessage returnResult = myMQTTClient.publishAndReturnResult(cmd, 1, GsonUtils.parseJSON(map), 3500, cmdReturn,true, "[\\S\\s]*((cmd)+).+((status)+)[\\S\\s]*");
		if (returnResult==null){
			return R.fail("40001","台灯无响应-未返回status");
		}
		Map<String, Object> oldData = (Map<String, Object>) GsonUtils.toMap( new String( returnResult.getPayload())).get("data");

		Map<String,Object> pub=new HashMap<>();
		pub.put("cmd", "config");
		pub.put("sender", "13888888888");
		pub.put("time", System.currentTimeMillis()/1000L);
		Map<String,Object> data=new HashMap<>();
		data.put("voiceLevel",voiceLevel);
		data.put("lightMode",((Number)oldData.get("lightMode")).intValue());
		data.put("brightnessLevel",((Number)oldData.get("brightnessLevel")).intValue());
		pub.put("data", data);

		MqttMessage publishAndReturnResult = myMQTTClient.publishAndReturnResult(cmd, 1, GsonUtils.parseJSON(pub), 3500, cmdReturn,true, "[\\S\\s]*((cmd)+).+((config)+)[\\S\\s]*");
		if (publishAndReturnResult==null){
			return R.fail("40001","台灯无响应");
		}
		return R.ok();
	}
	
	@RequestMapping("/config-position-status")
	@ResponseBody
	public R configPositionCheckStatus(String deviceUuid){

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

 		String cmdReturn=deviceUuid+"/cmdReturn";;
 		String cmd=deviceUuid+"/cmd";;
 		Map<String,Object> map=new HashMap<>();
		map.put("cmd", "positionCheckStatus");
		map.put("sender", "13888888888");
		map.put("time", System.currentTimeMillis()/1000L);
 		MqttMessage returnResult = myMQTTClient.publishAndReturnResult(cmd, 1, GsonUtils.parseJSON(map), 3500, cmdReturn,true, "[\\S\\s]*((cmd)+).+((positionCheckStatus)+)[\\S\\s]*");
		if (returnResult==null){
			return R.fail("40001","台灯无响应");

		}
 		
		return R.ok().add("returnMsg", new String(returnResult.getPayload()));
	}
	
	@RequestMapping("/config-position")
	@ResponseBody
	public R configCheck(String deviceUuid,Integer warnVoice,Integer checkWay,Integer on,Integer headBaseLine,Integer warnDropPercent){

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

 		String cmdReturn=deviceUuid+"/cmdReturn";;
 		String cmd=deviceUuid+"/cmd";;
 		Map<String,Object> map=new HashMap<>();
		map.put("cmd", "status");
		map.put("sender", "13888888888");
		map.put("time", System.currentTimeMillis()/1000L);
 		MqttMessage returnResult = myMQTTClient.publishAndReturnResult(cmd, 1, GsonUtils.parseJSON(map), 3500, cmdReturn,true, "[\\S\\s]*((cmd)+).+((status)+)[\\S\\s]*");
		if (returnResult==null){
			return R.fail("40001","台灯无响应-未返回status");
		}
		//发布实际内容
		map=new HashMap<>();
		Map<String,Object> data=new HashMap<>();
		map.put("data", data);
		map.put("cmd", "positionCheckConfig");
		map.put("sender", "13888888888");
		map.put("time", System.currentTimeMillis()/1000L);
		data.put("warnVoice", warnVoice);
		data.put("on", on);

		data.put("checkWay", checkWay);
		data.put("headBaseLine", headBaseLine);
		MqttMessage publishAndReturnResult = myMQTTClient.publishAndReturnResult(cmd, 1, GsonUtils.parseJSON(map), 3500, cmdReturn,true, "[\\S\\s]*((cmd)+).+((postionCheckConfig)+)[\\S\\s]*");
		if (publishAndReturnResult==null){
			return R.fail("40001","台灯无响应");
		}
		
 		
		return R.ok().add("returnMsg", new String(publishAndReturnResult.getPayload()));
	}
	
	@RequestMapping("/adjust-brightness")
	@ResponseBody
	public R adjustBrightness(Integer brightnessLevel,String deviceUuid){
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
 		String cmdReturn=deviceUuid+"/cmdReturn";;
 		String cmd=deviceUuid+"/cmd";;
		Map<String,Object> map=new HashMap<>();
		map.put("cmd", "status");
		map.put("sender", "13888888888");
		map.put("time", System.currentTimeMillis()/1000L);
		MqttMessage returnResult = myMQTTClient.publishAndReturnResult(cmd, 1, GsonUtils.parseJSON(map), 3500, cmdReturn,true, "[\\S\\s]*((cmd)+).+((status)+)[\\S\\s]*");
		if (returnResult==null){
			return R.fail("40001","台灯无响应-未返回status");
		}
		Map<String, Object> oldData = (Map<String, Object>) GsonUtils.toMap( new String( returnResult.getPayload())).get("data");
		Map<String,Object> pub=new HashMap<>();
		pub.put("cmd", "config");
		pub.put("sender", "13888888888");
		pub.put("time", System.currentTimeMillis()/1000L);
		Map<String,Object> data=new HashMap<>();
		data.put("brightnessLevel",brightnessLevel);
		data.put("lightMode",((Number)oldData.get("lightMode")).intValue());
		data.put("voiceLevel",((Number)oldData.get("voiceLevel")).intValue());

		pub.put("data", data);
		
		MqttMessage publishAndReturnResult = myMQTTClient.publishAndReturnResult(cmd, 1, GsonUtils.parseJSON(pub), 3500, cmdReturn,true, "[\\S\\s]*((cmd)+).+((config)+)[\\S\\s]*");
		if (publishAndReturnResult==null){
			return R.fail("40001","台灯无响应");
		}
		return R.ok();
	}
	
	@RequestMapping("/adjust-lightMode")
	@ResponseBody
	public R adjustLightMode(Integer lightMode,String deviceUuid){
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
 		String cmdReturn=deviceUuid+"/cmdReturn";;
 		String cmd=deviceUuid+"/cmd";;
		Map<String,Object> map=new HashMap<>();
		map.put("cmd", "status");
		map.put("sender", "13888888888");
		map.put("time", System.currentTimeMillis()/1000L);
		MqttMessage returnResult = myMQTTClient.publishAndReturnResult(cmd, 1, GsonUtils.parseJSON(map), 3500, cmdReturn,true, "[\\S\\s]*((cmd)+).+((status)+)[\\S\\s]*");
		if (returnResult==null){
			return R.fail("40001","台灯无响应-未返回status");
		}
		Map<String, Object> oldData = (Map<String, Object>) GsonUtils.toMap( new String( returnResult.getPayload())).get("data");

		Map<String,Object> pub=new HashMap<>();
		pub.put("cmd", "config");
		pub.put("sender", "13888888888");
		pub.put("time", System.currentTimeMillis()/1000L);
		Map<String,Object> data=new HashMap<>();
		data.put("lightMode",lightMode);
		data.put("voiceLevel",((Number)oldData.get("voiceLevel")).intValue());
		data.put("brightnessLevel",((Number)oldData.get("brightnessLevel")).intValue());
	
		pub.put("data", data);
		
		MqttMessage publishAndReturnResult = myMQTTClient.publishAndReturnResult(cmd, 1, GsonUtils.parseJSON(pub), 3500, cmdReturn,true, "[\\S\\s]*((cmd)+).+((config)+)[\\S\\s]*");
		if (publishAndReturnResult==null){
			return R.fail("40001","台灯无响应");
		}
		return R.ok();
	}
	
	
	@RequestMapping("/capture")
	@ResponseBody
	public R capture(String deviceUuid,HttpServletResponse res){
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
 		String capturetopic=deviceUuid+"/capture";;
 		String cmd=deviceUuid+"/cmd";;
 		int requestTimeStamp=new Long( System.currentTimeMillis()/1000).intValue();
		Map<String,Object> map=new HashMap<>();
		map.put("cmd", "capture");
		map.put("sender", "13888888888");
		map.put("time", requestTimeStamp);
		CaptureWaiter waiter=new CaptureWaiter(requestTimeStamp, 5000);
		ProgresssPromoter promoter = waiter.getProgresssPromoter();
		DeviceTestWebSocket.CAPTURE_PROGRESS_LIST.put(deviceUuid, promoter);
		byte[] imgByte= (byte[])  myMQTTClient.publishWithWaiter(cmd, 1, GsonUtils.parseJSON(map), 5000, capturetopic, true, waiter);
		DeviceTestWebSocket.CAPTURE_PROGRESS_LIST.remove(deviceUuid);
		if (imgByte==null){
			return R.fail("40001","台灯无响应").add("img", "");
		}else{
			return R.ok().add("img", Base64.encode(imgByte));
			
		}
	}
	
	/**
	 * 获取测试日志
	 * @author haowen
	 * @time 2018年11月12日下午5:58:03
	 * @Description  
	 * @param deviceUuid
	 * @param res
	 * @return
	 */
	@RequestMapping("/test-log")
	@ResponseBody
	public R getTestLog(String deviceUuid,HttpServletResponse res){
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
 		String capturetopic=deviceUuid+"/capture";;
 		String cmd=deviceUuid+"/cmd";;
 		int requestTimeStamp=new Long( System.currentTimeMillis()/1000).intValue();
		Map<String,Object> map=new HashMap<>();
		map.put("cmd", "capture");
		map.put("sender", "13888888888");
		map.put("time", requestTimeStamp);
		CaptureWaiter waiter=new CaptureWaiter(requestTimeStamp, 5000);
		ProgresssPromoter promoter = waiter.getProgresssPromoter();
		DeviceTestWebSocket.CAPTURE_PROGRESS_LIST.put(deviceUuid, promoter);
		byte[] imgByte= (byte[])  myMQTTClient.publishWithWaiter(cmd, 1, GsonUtils.parseJSON(map), 5000, capturetopic, true, waiter);
		DeviceTestWebSocket.CAPTURE_PROGRESS_LIST.remove(deviceUuid);
		if (imgByte==null){
			return R.fail("40001","台灯无响应").add("img", "");
		}else{
			return R.ok().add("log", new String(imgByte) );
			
		}
	}
	
	
	
	@RequestMapping("/send-internal-Voice")
	@ResponseBody
	public R sendInternalVoice(String deviceUuid,Integer index){
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
 		String cmdReturn=deviceUuid+"/cmdReturn";;
 		String cmd=deviceUuid+"/cmd";;
 		int requestTimeStamp=new Long( System.currentTimeMillis()/1000).intValue();
 		String sender= "13888888888";
		Map<String,Object> map=new HashMap<>();
		map.put("cmd", "voice");
		map.put("fileNum", index);
		map.put("sender", "13888888888");
		map.put("time",requestTimeStamp);
		
		MqttMessage returnResult = myMQTTClient.publishAndReturnResult(cmd, 1, GsonUtils.parseJSON(map), 5000, cmdReturn,true, "[\\S\\s]*((cmd)+).+((voiceRecv)+)[\\S\\s]*[\\S\\s]*");
        if (returnResult==null){
			return R.fail("40001","台灯无响应");
		}
		Map<String, Object> returnJson=GsonUtils.toMap( new String(  returnResult.getPayload()));
		if (!"10000".equals(  returnJson.get("msgCode"))){
			return R.fail("40001","设备正忙");
		}
		return R.ok("10001","播放成功");
	}

	@Autowired
	private VoicePublisherListener voicePublisher;
	@RequestMapping("/sendVoice")
	@ResponseBody
	public R sendVoice(String deviceUuid,Integer index){
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
 		String cmdReturn=deviceUuid+"/cmdReturn";;
 		String cmd=deviceUuid+"/cmd";;
 		String voice=deviceUuid+"/voice";;
 		int requestTimeStamp=new Long( System.currentTimeMillis()/1000).intValue();
 		String sender= "13888888888";
		Map<String,Object> map=new HashMap<>();
		map.put("cmd", "voice");
		map.put("fileNum", 0);
		map.put("sender", "13888888888");
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
		File file =new File(PathUtils.getTomcatPath(),"web/static/voice/L"+(index+1)+".mp3");
		DataDivider divider = LampDataUtils.getDivider(HeaderFlag.FLAG_55AA55AA, LampDataUtils.CMD_VOICE, 512-16, requestTimeStamp, file, true);
		try {
			List<byte[]> data = divider.getData();
			for(int i=0;i<data.size();i++){
				LogUtils.info("sending{}...", i);
				if (i<data.size()-1){
					//myMQTTClient.publish(voice, 1, data.get(i));
					myMQTTClient.publishAsyn(voice,1,data.get(i));
					PubAcker acker=new PubAcker(voice,1000);
					voicePublisher.addAndWait(acker);
					ThreadUtils.sleep(30);
				//	voicePublisher.
					continue;
				}
				returnResult = myMQTTClient.publishAndReturnResult(voice, 1, data.get(i) , 3000, cmdReturn, true,  "[\\S\\s]*((cmd)+).+((voiceRecv)+)[\\S\\s]*");
				if (returnResult==null){
					return R.fail("40001","台灯无响应，本次发送的包个数："+data.size());
				}
				returnJson=GsonUtils.toMap( new String(  returnResult.getPayload()));
				if ("10000".equals(  returnJson.get("msgCode"))){
					return R.ok("10001","播放成功，本次发送的包个数："+data.size());
				}else{
					return R.fail("40001","播放失败，本次发送的包个数："+data.size());
				}
			}
		} catch (Exception e) {
 			e.printStackTrace();
		}
		return R.fail();
	}
	
}
