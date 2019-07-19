package com.gzseeing.sys.mymqtt.waiter;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.gzseeing.utils.LampDataUtils;
import com.gzseeing.utils.LogUtils;
import com.gzseeing.utils.LampDataUtils.DataFrameReader;
import com.gzseeing.utils.ThreadUtils;
import com.haowen.mqtt.comp.waiter.impl.AbstractWaiter;

public class CaptureWaiter extends AbstractWaiter {

	
	private Integer requestTimeStamp;
	
	private Integer timeOut;
	
	public class ProgresssPromoter{
		private Integer total=0;
		private Double percent=0d;
		private Integer current=0;
		
		private final Map<String, Object> extras=new ConcurrentHashMap<>();

		public Integer getTotal() {
			return total;
		}

		public void setTotal(Integer total) {
			this.total = total;
		}

		public Double getPercent() {
			return percent;
		}

		public void setPercent(Double percent) {
			this.percent = percent;
		}

		public Integer getCurrent() {
			return current;
		}

		public void setCurrent(Integer current) {
			if (current==null){
				return;
			}
			this.current = current;
			if (total!=null&&total!=0){
				percent=1d*current/total;
			}
		}

		public Map<String, Object> getExtras() {
			return extras;
		}
		
		
		
		
	}
	
	private ProgresssPromoter progresssPromoter=new ProgresssPromoter();
	
	public CaptureWaiter(Integer requestTimeStamp, Integer waitUpSecond) {
		super();
		this.requestTimeStamp = requestTimeStamp;
		this.timeOut = waitUpSecond;
	}
 
	private  class WaitUpThread extends Thread{
		
		long lastTime=System.currentTimeMillis();
		public void run() {
			while(System.currentTimeMillis()-lastTime<=timeOut){
				ThreadUtils.sleep500Millis();
			}
			CaptureWaiter.this.stopWait();
			if (progresssPromoter.getTotal()==0||progresssPromoter.getTotal()!=datas.size()){
				LogUtils.info("<<<<<<<<<<get pic fail>>>>>>>>>>>");
			}

		};
		public void updateTime(){
			lastTime=System.currentTimeMillis();
		}
	} ;
	private  WaitUpThread w=new WaitUpThread();
	
	private   ConcurrentHashMap<Integer, DataFrameReader> datas=new ConcurrentHashMap<>();

	@Override
	public boolean collectData(String topic, MqttMessage message) {

		byte[] payload = message.getPayload();
		DataFrameReader frameReader = LampDataUtils.getFrameReader(payload,true);
		if (frameReader.getTimeStamp()==requestTimeStamp){
			datas.put(frameReader.getCurrentPackage(), frameReader);
			progresssPromoter.setTotal(frameReader.getTotalPackage());
			w.updateTime();
		}
		int size = datas.size();
		progresssPromoter.setCurrent(size);
		if (size>0){
			DataFrameReader reader = datas.entrySet().iterator().next().getValue();
			if (reader.getTotalPackage()==datas.size()){
				stopWait();
				LogUtils.info("<<<<<<<<<<get pic success>>>>>>>>>>>");
				return true;
			}
  		}
		return false;
	}

	
	
 
	
	@Override
	public Object getMsg() {
		w.start();
		w.updateTime();
		waitFor();
		byte[] bytes = LampDataUtils.combineDataToBytes(new ArrayList<>(datas.values()));
		return bytes;
	}





	@Override
	public boolean waitFor() {
		ThreadUtils.WaitWithSyn(CaptureWaiter.this); 
		return false;
	}





	@Override
	public boolean stopWait() {
		ThreadUtils.notifyALLWithSyn(CaptureWaiter.this);;
		 return false;
	}





	public ProgresssPromoter getProgresssPromoter() {
		return progresssPromoter;
	}
	
	
	

}
