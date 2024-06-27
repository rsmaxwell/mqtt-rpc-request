package com.rsmaxwell.mqtt.rpc.request.requests;

import com.rsmaxwell.mqtt.rpc.common.Request;
import com.rsmaxwell.mqtt.rpc.common.Response;

public class GetPages extends RpcRequest {

	public GetPages() {
		Request request = new Request("getPages");
		setRequest(request);
	}

	@Override
	public void handle(Response response) throws Exception {
		String result = response.getString("result");
		System.out.printf("result: %s\n", result);
	}
}
