package com.rsmaxwell.mqtt.rpc.requestor;

import org.junit.jupiter.api.Test;

import com.rsmaxwell.mqtt.rpc.common.buildinfo.BuildInfo;

class BuildInfoTest {

	@Test
	void printsBuildInfo() throws Exception {
		BuildInfo info = new BuildInfo();
		info.printAll();
	}
}