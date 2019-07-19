package com.gzseeing.sys.mymqtt.waiter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.gzseeing.utils.LampDataUtils;
import com.gzseeing.utils.LogUtils;
import com.gzseeing.utils.LampDataUtils.DataFrameReader;
import com.gzseeing.utils.ThreadUtils;
import com.haowen.mqtt.comp.waiter.impl.AbstractWaiter;

/**
 * 计算获得的包数，当发生无法继续回收的时候，返回用时，
 * @author haowen
 *
 */
public class CaptureCountWaiter extends AbstractWaiter {

	
	private Integer requestTimeStamp;
	
	private Integer timeOut;
	
	
	
	/**
	 * 测试结果提示
	 * @author haowen
	 *
	 */
	public class ProgresssPromoter{
		private Integer total=0;
		private Double percent=0d;
		private long startTime=0L;
		private long endTime=0L;

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
			return recvSize.get();
		}

		public void setCurrent() {
			if (total!=null&&total!=0){
				percent=1d*recvSize.get()/total;
			}
		}

		public long getTimeUsed() {
			return endTime-startTime;
		}

		public long getStartTime() {
			return startTime;
		}

		public void setStartTime(long startTime) {
			this.startTime = startTime;
		}

		public long getEndTime() {
			return endTime;
		}

		public void setEndTime(long endTime) {
			this.endTime = endTime;
		}

		public Map<String, Object> getExtras() {
			return extras;
		}
		
		
		
		
	}
	
	private ProgresssPromoter progresssPromoter=new ProgresssPromoter();
	
	public CaptureCountWaiter(Integer requestTimeStamp, Integer waitUpSecond) {
		super();
		this.requestTimeStamp = requestTimeStamp;
		this.timeOut = waitUpSecond;
	}
 

	private  WaitUpThread w=new WaitUpThread();
	

	private  AtomicInteger recvSize=new AtomicInteger(0); 
	@Override
	public boolean collectData(String topic, MqttMessage message) {

		byte[] payload = message.getPayload();
		DataFrameReader frameReader = LampDataUtils.getFrameReader(payload,true);
		if (frameReader.getTimeStamp()==requestTimeStamp){
			recvSize.incrementAndGet();
			progresssPromoter.setCurrent();
			progresssPromoter.setTotal(frameReader.getTotalPackage());
			w.updateTime();
 		}
		if (recvSize.get()>0){
			if (progresssPromoter.getTotal()==recvSize.get()){
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
		progresssPromoter.setStartTime(System.currentTimeMillis());
		waitFor();
		progresssPromoter.setEndTime(System.currentTimeMillis());
		return recvSize.get();
	}





	@Override
	public boolean waitFor() {
		ThreadUtils.WaitWithSyn(CaptureCountWaiter.this); 
		return false;
	}





	@Override
	public boolean stopWait() {
		ThreadUtils.notifyALLWithSyn(CaptureCountWaiter.this);;
		 return false;
	}





	public ProgresssPromoter getProgresssPromoter() {
		return progresssPromoter;
	}
	
	/**
	 * 超时唤醒
	 * @author haowen
	 *
	 */
	private  class WaitUpThread extends Thread{
		
		long lastTime=System.currentTimeMillis();
		public void run() {
			while(System.currentTimeMillis()-lastTime<=timeOut){
				ThreadUtils.sleep500Millis();
			}
			CaptureCountWaiter.this.stopWait();
			if (progresssPromoter.getTotal()==0||progresssPromoter.getTotal()!=recvSize.get()){
				LogUtils.info("<<<<<<<<<<get pic fail>>>>>>>>>>>");
			}

		};
		
		public void updateTime(){
			lastTime=System.currentTimeMillis();
		}
	} ;
	

}
