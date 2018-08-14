package demo.api.websocket.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 服务端信息
 * @author Lynn Li
 */
public class WebSocketServerInfo {
    private static final String $SERVER_PROTOCOL = "tcp";
    private static final String $SERVER_HOST = "";
    private static final String $SERVER_PORT = "";
    private static final String SHAKE_URL = $SERVER_PROTOCOL+"://"+$SERVER_HOST+":"+$SERVER_PORT;
    private static final String USER_NAME = "";
    private static final String USER_CODE= "";
    private static final String $ENV = "";
    private static final String $SUB_TOPIC_DEPTH = "/api/ws/v1/market/depth/{symbol}";
    private static final String $SUB_TOPIC_KLINE = "/api/ws/v1/market/kline/{symbol}/{range}";
    private static final String ACCESS_KEY = "";
    private static final String ACCESS_KEY_MD5 = getAccessKeyMD5();
    private static final String EMAIL_ACCOUNT = "";
    private static final String EMAIL_ACCOUNT_MD5 = getEmailAccountMD5();

    private static String getAccessKeyMD5(){
        if(StringUtils.isEmpty(ACCESS_KEY)){
            throw new Error("ACCESS_KEY cannot be empty");
        }
        return DigestUtils.md5Hex(ACCESS_KEY);
    }

    private static String getEmailAccountMD5(){
        if(StringUtils.isEmpty(EMAIL_ACCOUNT)){
            throw new Error("EMAIL_ACCOUNT cannot be empty");
        }
        return DigestUtils.md5Hex(EMAIL_ACCOUNT);
    }

    /**
     * 订阅深度图主题
     * @return
     */
    public static String getMarketDepthTopic(String symbol){
        return $ENV+$SUB_TOPIC_DEPTH.replace("{symbol}",symbol);
    }

    /**
     * 获取K线订阅目标
     * @param symbol
     * @param range
     * @return
     */
    public static String getKLineTopic(String symbol,long range){
        String rangeStr = Long.toString(range);
        return $ENV+$SUB_TOPIC_KLINE.replace("{symbol}",symbol).replace("{range}",rangeStr);
    }

    /**
     * 获取握手地址
     * @return
     */
    public static String getShakeUrl(){
        return SHAKE_URL;
    }

    /**
     * 客户端识别号
     * @return
     */
    public static String getClientId(String topic) {
        return ACCESS_KEY_MD5 + EMAIL_ACCOUNT_MD5 + DigestUtils.md5Hex(topic);
    }

    /**
     * 用户名
     * @return
     */
    public static String getUserName() {
        return USER_NAME;
    }

    /**
     * 用户识别码
     * @return
     */
    public static String getUserCode() {
        return USER_CODE;
    }
}
