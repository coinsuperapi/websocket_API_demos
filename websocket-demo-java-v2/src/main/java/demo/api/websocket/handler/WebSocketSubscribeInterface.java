package demo.api.websocket.handler;

/**
 * WebSocket处理器扩展接口：申明订阅目标
 * @author Lynn Li
 */
public interface WebSocketSubscribeInterface {

    /**
     * 获取订阅目标
     * @return
     */
    String getSubscribe();
}
