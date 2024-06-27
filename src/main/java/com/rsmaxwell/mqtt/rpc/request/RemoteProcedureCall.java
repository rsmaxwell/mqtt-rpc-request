package com.rsmaxwell.mqtt.rpc.request;

import java.util.WeakHashMap;

import org.eclipse.paho.mqttv5.client.MqttAsyncClient;
import org.eclipse.paho.mqttv5.client.MqttCallback;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.MqttSubscription;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rsmaxwell.mqtt.rpc.common.Adapter;
import com.rsmaxwell.mqtt.rpc.common.Response;
import com.rsmaxwell.mqtt.rpc.common.Token;

public class RemoteProcedureCall {

	static int qos = 0;
	static String clientID = "requester";
	static WeakHashMap<String, MqttMessage> replies = new WeakHashMap<>();
	static WeakHashMap<String, Token> tokens = new WeakHashMap<>();
	static private ObjectMapper mapper = new ObjectMapper();

	private MqttAsyncClient client;
	private String responseTopic;

	public RemoteProcedureCall(MqttAsyncClient client, String responseTopic) throws MqttException {
		this.client = client;
		this.responseTopic = responseTopic;
	}

	// Subscribe to the response topic
	public void subscribe() throws Exception {
		MqttSubscription subscription = new MqttSubscription(responseTopic);
		System.out.printf("subscribing to: %s\n", responseTopic);
		client.subscribe(subscription).waitForCompletion();
	}

	public Token request(String topic, byte[] request) throws Exception {

		Token token = new Token();
		String correlID = token.getID();

		MqttMessage message = new MqttMessage(request);
		MqttProperties properties = new MqttProperties();
		properties.setResponseTopic(responseTopic);
		properties.setCorrelationData(correlID.getBytes());
		message.setProperties(properties);
		message.setQos(qos);

		System.out.printf(String.format("Publishing: %s to topic: %s with qos: %d\n", new String(request), topic, qos));
		System.out.printf(String.format("    replyTopic: %s\n", responseTopic));
		client.publish(topic, message).waitForCompletion();
		System.out.println("Message published");

		tokens.put(correlID, token);
		return token;
	}

	public MqttCallback getAdapter() {
		Adapter adapter = new Adapter() {

			@Override
			public void messageArrived(String topic, MqttMessage reply) throws Exception {
				System.out.println("RemoteProcedureCall.Adapter.messageArrived");

				MqttProperties properties = reply.getProperties();
				byte[] corrationData = properties.getCorrelationData();
				String correlID = new String(corrationData);

				System.out.printf("correlID: %s\n", correlID);

				Token token = tokens.get(correlID);
				if (token == null) {
					System.out.printf("Discarding reply because token is null\n");
					return;
				}

				replies.put(correlID, reply);
				token.completed();
			}
		};

		return adapter;
	}

	public Response waitForResponse(Token token) throws Exception {

		token.waitForCompletion();

		String correlID = token.getID();
		MqttMessage reply = replies.get(correlID);

		byte[] payload = reply.getPayload();
		return mapper.readValue(payload, Response.class);
	}
}
