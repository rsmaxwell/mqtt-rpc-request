package com.rsmaxwell.mqtt.rpc.request.requests;

import java.net.HttpURLConnection;
import java.util.Map;

import org.eclipse.paho.mqttv5.common.MqttMessage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rsmaxwell.mqtt.rpc.common.Request;
import com.rsmaxwell.mqtt.rpc.common.Response;

public abstract class RpcRequest {

	private Request request;

	private ObjectMapper mapper = new ObjectMapper();

	public void setRequest(Request request) {
		this.request = request;
	}

	public Request getRequest() {
		return request;
	}

	public Map<String, Object> checkReply(String topic, MqttMessage replyMessage) throws Exception {

		String payload = new String(replyMessage.getPayload());

		System.out.printf("message %s, topic: %s, qos: %d\n", payload, topic, replyMessage.getQos());
		System.out.printf("decoding message payload: %s\n", payload);

		Response reply = mapper.readValue(payload, Response.class);

		if (reply == null) {
			throw new Exception("discarding message because decoded message was null");
		}

		int code = reply.getInteger("code");
		if (code != HttpURLConnection.HTTP_OK) {
			String message = reply.getString("message");
			if (message == null) {
				throw new Exception(String.format("code: %d\n", code));
			} else {
				throw new Exception(String.format("code: %d, message: %s\n", code, message));
			}
		}

		return reply;
	}

	public abstract void handle(Response response) throws Exception;
}
