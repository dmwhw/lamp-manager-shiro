package com.gzseeing.sys.mymqtt;

import com.haowen.mqtt.comp.MyMQTTClient;
import com.haowen.mqtt.comp.listener.MqttDeliveryCompleteListener;
import com.haowen.mqtt.starter.annotation.MyMqttClientListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArraySet;

@MyMqttClientListener(clientName = "myMQTTClient")
public class VoicePublisherListener implements MqttDeliveryCompleteListener {



    private final static CopyOnWriteArraySet<PubAcker> list=new CopyOnWriteArraySet<>();

    @Override
    public void deliveryDone(MyMQTTClient myMQTTClient, IMqttDeliveryToken iMqttDeliveryToken) {
        try {
            MqttDeliveryToken token= (MqttDeliveryToken) iMqttDeliveryToken;
            iMqttDeliveryToken.waitForCompletion();
            Iterator<PubAcker> iterator = list.iterator();
            while(iterator.hasNext()){
                PubAcker next = iterator.next();
                if (next.collectMsg(iMqttDeliveryToken)){
                    list.remove(next);
                    next.stopWait();
                    return ;
                }
            }


        } catch ( Exception e) {
            e.printStackTrace();
        }
    }

    public void addAndWait(PubAcker acker){
        if (acker!=null){
            list.add(acker);
            acker.waitFor();
        }
    }
}
