package com.rsmaxwell.mqtt.rpc.request;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.eclipse.paho.mqttv5.client.MqttAsyncClient;
import org.eclipse.paho.mqttv5.client.MqttClientPersistence;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.persist.MqttDefaultFilePersistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rsmaxwell.mqtt.rpc.common.Response;
import com.rsmaxwell.mqtt.rpc.common.Token;
import com.rsmaxwell.mqtt.rpc.request.requests.GetPages;
import com.rsmaxwell.mqtt.rpc.request.requests.RpcRequest;

public class GetPagesTest {

	static int qos = 0;
	static volatile boolean keepRunning = true;

	static private ObjectMapper mapper = new ObjectMapper();

	public static void main(String[] args) throws Exception {

		Option serverOption = createOption("s", "server", "mqtt server", "URL of MQTT server", false);
		Option usernameOption = createOption("u", "username", "Username", "Username for the MQTT server", true);
		Option passwordOption = createOption("p", "password", "Password", "Password for the MQTT server", true);

		// @formatter:off
		Options options = new Options();
		options.addOption(serverOption)
			   .addOption(usernameOption)
			   .addOption(passwordOption);
		// @formatter:on

		CommandLineParser commandLineParser = new DefaultParser();
		CommandLine commandLine = commandLineParser.parse(options, args);
		String server = commandLine.hasOption("h") ? commandLine.getOptionValue(serverOption) : "tcp://127.0.0.1:1883";
		String username = commandLine.getOptionValue(usernameOption);
		String password = commandLine.getOptionValue(passwordOption);

		String clientID = "requester";
		String requestTopic = "request";

		MqttClientPersistence persistence = new MqttDefaultFilePersistence();
		MqttAsyncClient client = new MqttAsyncClient(server, clientID, persistence);
		MqttConnectionOptions connOpts = new MqttConnectionOptions();
		connOpts.setUserName(username);
		connOpts.setPassword(password.getBytes());

		RemoteProcedureCall rpc = new RemoteProcedureCall(client, String.format("response/%s", clientID));
		client.setCallback(rpc.getAdapter());

		// Connect
		System.out.printf("Connecting to broker: %s as '%s'\n", server, clientID);
		client.connect(connOpts).waitForCompletion();
		System.out.printf("Client %s connected\n", clientID);

		// Subscribe to the responseTopic
		rpc.subscribe();

		RpcRequest handler = new GetPages();
		byte[] request = mapper.writeValueAsBytes(handler.getRequest());
		Token token = rpc.request(requestTopic, request);

		// Wait for the response to arrive
		Response response = rpc.waitForResponse(token);
		handler.handle(response);

		// Disconnect
		client.disconnect().waitForCompletion();
		System.out.printf("Client %s disconnected\n", clientID);
		System.out.println("exiting");
	}

	static Option createOption(String shortName, String longName, String argName, String description, boolean required) {
		return Option.builder(shortName).longOpt(longName).argName(argName).desc(description).hasArg().required(required).build();
	}
}
