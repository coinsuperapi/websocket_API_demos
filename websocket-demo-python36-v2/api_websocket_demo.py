#!/usr/bin/env python
# -*- coding: utf-8; py-indent-offset:4 -*-
import hashlib
import ssl
import atexit
import paho.mqtt.client as mqtt

params = {
    "isSSL": False,
    "serverHost": "",
    "serverPort": 0,
    "env": "",
    "userName": "",
    "userCode": "",
    "apiAccessKey": "",
    "email": "",
    "clientId": None
}

topic = "/api/ws/v1/market/orderBook/BTC_USD/0.01"

params["clientId"] = hashlib.md5(params["apiAccessKey"].encode("utf8")).hexdigest() \
                     + hashlib.md5(params["email"].encode("utf8")).hexdigest() \
                     + hashlib.md5(topic.encode("utf8")).hexdigest()


def on_message(client, userdata, msg):
    print(msg.topic+" "+str(msg.payload))


def on_disconnect(client, userdata, rc):
    print("disconnected: " + str(rc))
    # do reconnect
    # client.loop_stop(True)


def on_log(mqttc, obj, level, string):
    print(string)


ssl_context = ssl.create_default_context()

isExit = False
mqttc = None


def subscribe_exe():
    transport = "tcp"
    if params["isSSL"]:
        transport = "websockets"
    mqttc = mqtt.Client(params["clientId"], False, False, mqtt.MQTTv311, transport)
    mqttc.username_pw_set(params["userName"], params["userCode"])
    # mqttc.ws_set_options(None)
    if params["isSSL"]:
        # print('isSSL')
        # ssl_context['check_hostname'] = None
        mqttc.tls_set_context(ssl_context)
        mqttc.tls_insecure_set(True)

    # log
    # mqttc.on_log = on_log
    mqttc.connect(params["serverHost"], params["serverPort"], 60)
    # mqttc.on_disconnect = on_disconnect
    mqttc.on_message = on_message
    mqttc.subscribe(params["env"] + topic)
    # print("subscribe...")
    # print(mqtt.error_string(11))
    rc = 0
    while rc == 0:
        rc = mqttc.loop_read(60)
    # print("reconnect...")
    mqttc.loop_stop(True)
    mqttc.disconnect()
    if not isExit:
        subscribe_exe()


def exit_disconnect():
    print("existed")
    mqttc.disconnect()


atexit.register(exit_disconnect)

subscribe_exe()
