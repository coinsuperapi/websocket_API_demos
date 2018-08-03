package demo.api.websocket.handler;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;

/**
 * 订阅消息处理器公共抽象类
 * @author Lynn Li
 */
public abstract class AbstractStringSessionHandler implements WebSocketSubscribeInterface,IMqttMessageListener {

}
