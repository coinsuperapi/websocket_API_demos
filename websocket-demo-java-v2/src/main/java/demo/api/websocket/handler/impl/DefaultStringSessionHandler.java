package demo.api.websocket.handler.impl;

import demo.api.websocket.handler.AbstractStringSessionHandler;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * 默认订阅消息处理器，开发者可自行重写
 * @author Lynn Li
 */
public class DefaultStringSessionHandler extends AbstractStringSessionHandler {
    private String subscribe;

    public DefaultStringSessionHandler(String subscribe){
        this.subscribe = subscribe;
    }

    public String getSubscribe() {
        return subscribe;
    }

    @Deprecated
    public void setSubscribe(String subscribe) {
        this.subscribe = subscribe;
    }

    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        System.out.println(String.format("%s====subscribe[%s] received:\n%s",s,getSubscribe(),new String(mqttMessage.getPayload())));
    }


}
