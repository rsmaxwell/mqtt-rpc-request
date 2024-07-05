package com.rsmaxwell.mqtt.rpc.request.requests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.rsmaxwell.mqtt.rpc.common.AbstractRequest;
import com.rsmaxwell.mqtt.rpc.common.Response;

public class GetPages extends AbstractRequest {

	private static final Logger logger = LogManager.getLogger(GetPages.class);

	public GetPages() {
		super("getPages");
	}

	@Override
	public void handle(Response response) throws Exception {
		String result = response.getString("result");
		logger.info(String.format("result: %s", result));
	}
}
