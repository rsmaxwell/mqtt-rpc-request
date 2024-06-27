package com.rsmaxwell.mqtt.rpc.request.requests;

import com.rsmaxwell.mqtt.rpc.common.Request;
import com.rsmaxwell.mqtt.rpc.common.Response;

public class Quit extends RpcRequest {

	public Quit() {
		Request request = new Request("quit");
		setRequest(request);
	}

	@Override
	public void handle(Response response) throws Exception {
		System.out.println("Quit.handle");
	}
}
