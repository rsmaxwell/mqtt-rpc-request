package com.rsmaxwell.mqtt.rpc.request.requests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.rsmaxwell.mqtt.rpc.common.Request;
import com.rsmaxwell.mqtt.rpc.common.Response;
import com.rsmaxwell.mqtt.rpc.request.RpcRequest;

public class Calculator extends RpcRequest {

	private static final Logger logger = LogManager.getLogger(Calculator.class);

	public Calculator(String operation, int param1, int param2) {
		Request request = new Request("calculator");
		request.put("operation", operation);
		request.put("param1", param1);
		request.put("param2", param2);
		setRequest(request);
	}

	@Override
	public void handle(Response response) throws Exception {
		int result = response.getInteger("result");
		logger.info(String.format("result: %d", result));
	}
}
