package com.gzseeing.sys.mymqtt.waiter;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.haowen.mqtt.comp.waiter.impl.AbstractWaiter;

 
/**
 * 包含字符等待者,只检验一次。校验到后立刻返回
 * @author haowen
 *
 */
public class StringsContentWaiter extends AbstractWaiter {
	
	private Integer timeOut;

	private String waitForTopic;
	
	private MqttMessage msg; 
	
	private  Set<String>  contentSet;
	/**
	 * 是否已经超时或者已经解决完了
	 */
	private boolean isDone=false;
	
	public StringsContentWaiter(Integer timeOut, String waitForTopic, String[] contentArrays) {
		super();
		this.timeOut = timeOut;
		this.waitForTopic = waitForTopic;
		this.contentSet=Collections.unmodifiableSet( new HashSet<>(Arrays.asList(contentArrays)));
	}


	public Integer getTimeOut() {
		return timeOut;
	}


	public String getWaitForTopic() {
		return waitForTopic;
	}




	public Set<String>  getContentSet() {
		return contentSet;
	}


	public boolean collectData(String topic,MqttMessage message){
		synchronized (this) {
			if (isDone){
				return true;//返回true表示结束，不再参与对比
			}
		}
		if (topic.equals(waitForTopic)&&contentSet==null ){
			this.msg=message;
			synchronized (this) {
				this.notify();	
			}
			return true;//返回true表示结束，不再参与对比
		}
		
		if (topic.equals(waitForTopic)&&contentSet!=null ){
			byte[] payload = message.getPayload(); 
			if ( payload==null  ){
				return false;
			}
			String payloadContent=new String(payload);
			boolean isMatch=true;
			for (String  content : contentSet) {
				if (!payloadContent.contains(content)){
					isMatch=false;
 					break;
				}
			}
			if (!isMatch){
				//不符合，等待下一次
				return false;
			}
			//满足我们要的，立刻返回消息数据
			this.msg=message;
			synchronized (this) {
				this.notify();	
			}
			return true;
			 
			
		}
		return false;//不满足，继续检查下一个包
	}
	
	public MqttMessage getMsg( ){
			synchronized (this) {
			try {
				this.wait(timeOut);
				isDone=true;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
			
			//waiters.remove(this);
		return msg;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contentSet == null) ? 0 : contentSet.hashCode());
		result = prime * result + ((timeOut == null) ? 0 : timeOut.hashCode());
		result = prime * result + ((waitForTopic == null) ? 0 : waitForTopic.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StringsContentWaiter other = (StringsContentWaiter) obj;
		if (contentSet == null) {
			if (other.contentSet != null)
				return false;
		} else if (!contentSet.equals(other.contentSet))
			return false;
		if (timeOut == null) {
			if (other.timeOut != null)
				return false;
		} else if (!timeOut.equals(other.timeOut))
			return false;
		if (waitForTopic == null) {
			if (other.waitForTopic != null)
				return false;
		} else if (!waitForTopic.equals(other.waitForTopic))
			return false;
		return true;
	}





 
}
