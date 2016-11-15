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
		final Config config = ConfigFactory.defaultApplication();
		PageAccessTokenProvider tokenProvider = new PageAccessTokenProvider(config);
		final PageAccessToken pageAccessToken = tokenProvider.getToken();
		FacebookPublisher facebookPublisher = new FacebookPublisher(config, pageAccessToken);
		new KafkaConsumer(facebookPublisher).run();
	}

}