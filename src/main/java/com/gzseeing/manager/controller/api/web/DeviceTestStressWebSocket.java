package com.gzseeing.manager.controller.api.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gzseeing.utils.GsonUtils;
import com.gzseeing.utils.LampDataUtils;
import com.gzseeing.utils.SpringContextUtils;
import com.gzseeing.utils.LampDataUtils.DataFrameReader;
import com.haowen.mqtt.comp.MyMQTTClient;

@ServerEndpoint(value = "/device-test/stress-test")
@Component
public class DeviceTestStressWebSocket implements Runnable {
	 //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的DeviceTestWebSocket对象。
    private static CopyOnWriteArraySet<DeviceTestStressWebSocket> webSocketSet = new CopyOnWriteArraySet<DeviceTestStressWebSocket>();

    
    private Thread sendThread ;
    
    @Autowired
    private MyMQTTClient myMQTTClient;
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    private String deviceUuid;
    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
       // System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
        sendMessage("msg");
  
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
       // System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {
       // System.out.println("来自客户端的消息:" + message);
        Map<String,Object> map=GsonUtils.toMap(message);
        if (map==null){
        	return ;
        }
    	String deviceuuid = (String) map.get("deviceUuid");
    	if (deviceuuid==null){
    		map.put("msgCode", "40000");
			sendMessage(GsonUtils.parseJSON(map));
			return ;
    	}
    	this.deviceUuid=deviceuuid;
    	//new Thread to Send
    	sendThread=new Thread(this);
    	sendThread.start();
      
    }

    /**
     * 发生错误时调用
     * */
    @OnError
    public void onError(Session session, Throwable error) {
       // System.out.println("发生错误");
        error.printStackTrace();
    }


    public void sendMessage(String message) {
        try {
			this.session.getBasicRemote().sendText(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
        //this.session.getAsyncRemote().sendText(message);
    }


    /**
     * 群发自定义消息
     * */
    public static void sendInfo(String message) throws IOException {
        for (DeviceTestStressWebSocket item : webSocketSet) {
           item.sendMessage(message);
        
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        DeviceTestStressWebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        DeviceTestStressWebSocket.onlineCount--;
    }

	@Override
	public void run() {
		myMQTTClient=SpringContextUtils.getContext().getBean(MyMQTTClient.class);
		while(true){
			if (this.session.isOpen()){
				MqttMessage mqttMessage = myMQTTClient.publishAndReturnResult(deviceUuid+"", 1, "", 60000, deviceUuid+"/capture", true, null);
	    		if (mqttMessage==null){
	    			continue;
	    		}
				Map<String, Object> map=new HashMap<>();
				map.put("msgCode", "10000");
				DataFrameReader frameReader = LampDataUtils.getFrameReader(mqttMessage.getPayload(), true);
				map.put("log",new String( frameReader.getData()));
				System.out.println(new String(frameReader.getData()));
				sendMessage(GsonUtils.parseJSON(map));
 			}else{
				break;
			}
		}
	}
}
