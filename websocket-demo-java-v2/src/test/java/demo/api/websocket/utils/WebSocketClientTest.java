package demo.api.websocket.utils;

import demo.api.websocket.handler.impl.DefaultStringSessionHandler;

public class WebSocketClientTest {

    public static void main(String[] args){
        //订阅K线
        DefaultStringSessionHandler kLineHandler = new DefaultStringSessionHandler(WebSocketServerInfo.getMarketDepthTopic("BTC/USD"));
        WebSocketClient.startKLineListener(kLineHandler);
        //订阅深度行情
        //DefaultStringSessionHandler marketDepthHandler = new DefaultStringSessionHandler(WebSocketServerInfo.getMarketDepthSubscribe("BTC/USD"));
        //WebSocketClient.startMarketDepthListener(marketDepthHandler);

        System.out.println("started Listener executors");
    }
}
