package com.rsmaxwell.mqtt.rpc.request;

import java.util.WeakHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.mqttv5.client.MqttAsyncClient;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.MqttSubscription;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rsmaxwell.mqtt.rpc.common.Adapter;

public class RemoteProcedureCall {

	private static final Logger logger = LogManager.getLogger(RemoteProcedureCall.class);

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

		client.setCallback(new Adapter() {

			@Override
			public void messageArrived(String topic, MqttMessage reply) throws Exception {
				logger.info("messageArrived");

				MqttProperties properties = reply.getProperties();
				byte[] corrationData = properties.getCorrelationData();

				if (corrationData == null) {
					logger.info("Discarding reply because corrationData is null");
					return;
				}

				String correlID = new String(corrationData);
				logger.info(String.format("correlID: %s", correlID));

				Token token = tokens.get(correlID);
				if (token == null) {
					logger.info("Discarding reply because token is null");
					return;
				}

				replies.put(correlID, reply);
				token.completed();
			}
		});
	}

	// Subscribe to the response topic
	public void subscribeToResponseTopic() throws Exception {
		MqttSubscription subscription = new MqttSubscription(responseTopic);
		logger.info(String.format("subscribing to: %s", responseTopic));
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

		logger.info(String.format("Publishing: %s to topic: '%s' with qos: %d, replyTopic: '%s'", new String(request), topic, qos, responseTopic));
		logger.info(String.format("correlID: %s", correlID));
		client.publish(topic, message);

		tokens.put(correlID, token);
		return token;
	}
}
