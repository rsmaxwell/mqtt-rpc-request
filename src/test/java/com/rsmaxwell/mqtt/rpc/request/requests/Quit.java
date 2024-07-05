package com.rsmaxwell.mqtt.rpc.request.requests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.rsmaxwell.mqtt.rpc.common.AbstractRequest;
import com.rsmaxwell.mqtt.rpc.common.Response;

public class Quit extends AbstractRequest {

	private static final Logger logger = LogManager.getLogger(Quit.class);

	public Quit() {
		super("quit");
	}

	@Override
	public void handle(Response response) throws Exception {
		logger.info("Quit.handle");
	}
}
