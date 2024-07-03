package com.rsmaxwell.mqtt.rpc.request;

public class Version {

	public static String version() {
		return "$VERSION";
	}

	public static String buildId() {
		return "$BUILD_ID";
	}

	public static String buildDate() {
		return "$TIMESTAMP";
	}

	public static String gitCommit() {
		return "$GIT_COMMIT";
	}

	public static String gitBranch() {
		return "$GIT_BRANCH";
	}

	public static String gitURL() {
		return "$GIT_URL";
	}
}
