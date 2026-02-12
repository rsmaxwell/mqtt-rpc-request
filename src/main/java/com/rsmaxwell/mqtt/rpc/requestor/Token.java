package com.rsmaxwell.mqtt.rpc.requestor;

import java.util.UUID;

import org.eclipse.paho.mqttv5.common.MqttMessage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rsmaxwell.mqtt.rpc.common.Response;

public class Token {

	private String id;
	static private ObjectMapper mapper = new ObjectMapper();

	public Token() {
		id = UUID.randomUUID().toString();
	}

	public String getID() {
		return id;
	}

	public Response waitForResponse() throws Exception {

		synchronized (this) {
			wait();
		}

		MqttMessage reply = RemoteProcedureCall.replies.get(id);

		byte[] payload = reply.getPayload();
		return mapper.readValue(payload, Response.class);
	}

	public synchronized void completed() throws InterruptedException {
		notifyAll();
	}
}
