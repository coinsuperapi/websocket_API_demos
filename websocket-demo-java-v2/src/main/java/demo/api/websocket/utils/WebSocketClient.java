package demo.api.websocket.utils;

import demo.api.websocket.handler.AbstractStringSessionHandler;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * WebSocket客户端工具（若交易所有新增的订阅接口，开发者可仿照以下方式自行扩展，或关注DEMO的更新）
 * @author Lynn Li
 */
public class WebSocketClient {
    /**初始化K线监听启动线程池*/
    private static final ExecutorService K_LINE_EXECUTOR = getSingleListenerExecutor("kline-listener-");
    /**初始化深度行情监听启动线程池*/
    private static final ExecutorService MARKET_DEPTH_EXECUTOR = getSingleListenerExecutor("market-depth-listener-");

    /**
     * 开始监听K线数据
     * @param handler
     */
    public static void startKLineListener(final AbstractStringSessionHandler handler){
        if(handler == null){
            throw new IllegalArgumentException("handler cannot be null");
        }
        K_LINE_EXECUTOR.submit(new Runnable() {
            public void run() {
                try {
                    runConnect(handler);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 开始监听深度行情数据
     * @param handler
     */
    public static void startMarketDepthListener(final AbstractStringSessionHandler handler){
        if(handler == null){
            throw new IllegalArgumentException("handler cannot be null");
        }
        MARKET_DEPTH_EXECUTOR.submit(new Runnable() {
            public void run() {
                try {
                    runConnect(handler);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 执行连接
     * @param handler
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private static void runConnect(AbstractStringSessionHandler handler) {
        int qos             = 2;
        String serverURI       = WebSocketServerInfo.getShakeUrl();
        String clientId     = WebSocketServerInfo.getClientId(handler.getSubscribe());
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            MqttClient sampleClient = new MqttClient(serverURI, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setUserName(WebSocketServerInfo.getUserName());
            connOpts.setPassword(WebSocketServerInfo.getUserCode().toCharArray());

            System.out.println("Connecting to serverURI: "+serverURI);
            sampleClient.connect(connOpts);
            System.out.println("Connected");


            System.out.println("Subscribing "+handler.getSubscribe());
            sampleClient.subscribe(handler.getSubscribe(), qos, handler);
            System.out.println("Subscribed "+handler.getSubscribe());
        } catch(MqttException me) {
            me.printStackTrace();
        }
    }

    /**
     * 获取单线程线程池
     * @param threadNamePrefix
     * @return
     */
    private static ThreadPoolExecutor getSingleListenerExecutor(final String threadNamePrefix) {
        return new ThreadPoolExecutor(1, 1,
                10000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(),new WebSocketThreadFactory(threadNamePrefix));
    }

    static class WebSocketThreadFactory implements ThreadFactory{
        private static AtomicInteger index = new AtomicInteger(0);
        private String threadName;

        public WebSocketThreadFactory(String prefixName){
            this.threadName = "ThreadPool-"+index.addAndGet(1)+"-"+prefixName;
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setName(threadName);
            return t;
        }
    }
}
