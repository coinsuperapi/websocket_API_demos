using System.Text;
using System.Security.Cryptography;
using uPLibrary.Networking.M2Mqtt.Messages;

namespace uPLibrary.Networking.M2Mqtt
{
    class RunDemo
    {
        //服务器信息
        public const string SERVER_HOST = "put host here";//TODO: 服务端host
        public const string USER_NAME = "put subscribe user name here";//TODO: 订阅者用户名
        public const string USER_CODE = "put subscribe user code here";//TODO: 订阅者识别码
        public const string API_ENV = "put env here";//TODO: $env
        //用户账户信息
        public const string MY_ACCOUNT_API_ACCESS_KEY = null;//TODO: API access_key【必填】
        public const string MY_ACCOUNT_EMAIL = null;//TODO: 账户email【必填】
        public static string MY_ACCOUNT_SIGN = MD5Encrypt(MY_ACCOUNT_API_ACCESS_KEY) + MD5Encrypt(MY_ACCOUNT_EMAIL);
        //订阅信息
        public const string API_TOPIC_ORDER_BOOK_001 = "/api/ws/v1/market/orderBook/BTC_USD/0.01";//订阅主题($subTopic)
        public const string FULL_TOPIC = API_ENV + API_TOPIC_ORDER_BOOK_001;

        /// <summary>
        /// DEMO方法
        /// </summary>
        /// <param name="args"></param>
        static void Main(string[] args)
        {
            string clientId = GetClientId(FULL_TOPIC);
            MqttClient client = new MqttClient(SERVER_HOST);
            // message received handler
            client.MqttMsgPublishReceived += client_MqttMsgPublishReceived;
            client.Connect(clientId, USER_NAME, USER_CODE);
            client.Subscribe(new string[] { FULL_TOPIC }, new byte[] { MqttMsgBase.QOS_LEVEL_EXACTLY_ONCE });
            System.Diagnostics.Debug.WriteLine("subscribed");
        }

        /// <summary>
        /// 接收订阅消息的处理器
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        static void client_MqttMsgPublishReceived(object sender, MqttMsgPublishEventArgs e)
        {
            string msg = Encoding.UTF8.GetString(e.Message);
            System.Diagnostics.Debug.WriteLine(msg);
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="topicId"></param>
        /// <returns></returns>
        public static string GetClientId(string topicId)
        {
            return MY_ACCOUNT_SIGN + MD5Encrypt(topicId);
        }

        /// <summary>
        /// 给一个字符串进行MD5加密
        /// </summary>
        /// <param name="strText">待加密字符串</param>
        /// <returns>加密后的字符串</returns>
        public static string MD5Encrypt(string strText)
        {
            //MD5 md5 = new MD5CryptoServiceProvider();
            byte[] result = MD5.Create().ComputeHash(System.Text.Encoding.UTF8.GetBytes(strText));
            var md5Sign = new StringBuilder();
            for (int i = 0; i < result.Length; i++)
            {
                var hex2 = result[i].ToString("x");
                //32位md5，单字节1位时需补0
                if (hex2.Length < 2)
                {
                    hex2 = "0" + hex2;
                }
                md5Sign.Append(hex2);
            }
            return md5Sign.ToString();
        }
    }
}
