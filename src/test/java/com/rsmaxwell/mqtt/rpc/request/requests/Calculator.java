package com.rsmaxwell.mqtt.rpc.request.requests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.rsmaxwell.mqtt.rpc.common.AbstractRequest;
import com.rsmaxwell.mqtt.rpc.common.Response;

public class Calculator extends AbstractRequest {

	private static final Logger logger = LogManager.getLogger(Calculator.class);

	public Calculator(String operation, int param1, int param2) {
		super("calculator");
		put("operation", operation);
		put("param1", param1);
		put("param2", param2);
	}

	@Override
	public void handle(Response response) throws Exception {
		int result = response.getInteger("result");
		logger.info(String.format("result: %d", result));
	}
}
