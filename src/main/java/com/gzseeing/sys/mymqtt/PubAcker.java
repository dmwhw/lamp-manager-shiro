package com.gzseeing.sys.mymqtt;


import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;

import java.util.Objects;

public class PubAcker {

	private String pubTopic;
	
	private Integer timeOut=1000;

	public PubAcker(String pubTopic, Integer timeOut) {
		this.pubTopic = pubTopic;
		this.timeOut = timeOut;
	}

	private Object obj;
	
	public boolean collectMsg(IMqttDeliveryToken token){
		if (token!=null&&token.isComplete() ){
			obj=token;
			return true;
		}
		return false;
	}
	
	public Object get(){
		return this.obj;
	}
	
	
	public  boolean waitFor(){
		synchronized (this) {
			try {
				this.wait(timeOut);
			} catch (Exception e) {
 				e.printStackTrace();
			}
		}
		return false;
	}
	
	public  boolean stopWait(){
		synchronized (this) {
			try {
				this.notifyAll();
			} catch (Exception e) {
 				e.printStackTrace();
			}
		}
		return false;
	}


	public String getPubTopic() {
		return pubTopic;
	}


	public void setPubTopic(String pubTopic) {
		this.pubTopic = pubTopic;
	}


	public Integer getTimeOut() {
		return timeOut;
	}


	public void setTimeOut(Integer timeOut) {
		this.timeOut = timeOut;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PubAcker pubAcker = (PubAcker) o;
		return Objects.equals(pubTopic, pubAcker.pubTopic) &&
				Objects.equals(timeOut, pubAcker.timeOut);
	}

	@Override
	public int hashCode() {
		return Objects.hash(pubTopic, timeOut);
	}
}
