package com.jvm_bloggers.facebook.client;

import com.jvm_bloggers.facebook.client.fb.FacebookPublisher;
import com.jvm_bloggers.facebook.client.kafka.KafkaConsumer;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class Application {

	public static void main(String[] args) {
		final Config config = ConfigFactory.defaultApplication();
		FacebookPublisher facebookPublisher = new FacebookPublisher(config);
		new KafkaConsumer(facebookPublisher).run();
	}

}