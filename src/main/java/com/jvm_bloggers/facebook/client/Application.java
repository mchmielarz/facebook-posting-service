package com.jvm_bloggers.facebook.client;

import com.jvm_bloggers.facebook.client.kafka.KafkaConsumer;

public class Application {

	public static void main(String[] args) {
		new KafkaConsumer().run();
	}

}