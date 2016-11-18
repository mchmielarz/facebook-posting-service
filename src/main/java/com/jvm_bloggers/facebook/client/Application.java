package com.jvm_bloggers.facebook.client;

import com.jvm_bloggers.facebook.client.fb.FacebookPublisher;
import com.jvm_bloggers.facebook.client.fb.PageAccessToken;
import com.jvm_bloggers.facebook.client.fb.PageAccessTokenProvider;
import com.jvm_bloggers.facebook.client.kafka.KafkaConsumer;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.IOException;

public class Application {

	public static void main(String[] args) throws IOException {
		final Config fbConfig = getFacebookConfig();
		final PageAccessToken pageAccessToken = getPageAccessToken(fbConfig);
		FacebookPublisher facebookPublisher = new FacebookPublisher(fbConfig, pageAccessToken);
		new KafkaConsumer(facebookPublisher).run();
	}

	private static PageAccessToken getPageAccessToken(Config fbConfig) throws IOException {
		return new PageAccessTokenProvider(fbConfig).getToken();
	}

	private static Config getFacebookConfig() {
		final Config systemProperties = ConfigFactory.systemProperties();
		return ConfigFactory.load().withFallback(systemProperties).getConfig("facebook");
	}

}