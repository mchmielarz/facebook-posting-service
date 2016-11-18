package com.jvm_bloggers.facebook.client;

import com.jvm_bloggers.facebook.client.fb.FacebookPublisher;
import com.jvm_bloggers.facebook.client.fb.PageAccessToken;
import com.jvm_bloggers.facebook.client.fb.PageAccessTokenProvider;
import com.jvm_bloggers.facebook.client.kafka.KafkaConsumer;
import com.jvm_bloggers.facebook.client.templates.MessageDataProvider;
import com.jvm_bloggers.facebook.client.templates.MessageTemplateProvider;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.IOException;

public class Application {

	public static void main(String[] args) throws IOException {
		final Config appConfig = getApplicationConfig();
		FacebookPublisher facebookPublisher = facebookPublisher(appConfig);
		new KafkaConsumer(facebookPublisher).run();
	}

	private static Config getApplicationConfig() {
		final Config systemProperties = ConfigFactory.systemProperties();
		return ConfigFactory.load().withFallback(systemProperties);
	}

	private static PageAccessToken getPageAccessToken(Config fbConfig) throws IOException {
		return new PageAccessTokenProvider(fbConfig).getToken();
	}

	private static FacebookPublisher facebookPublisher(Config appConfig) throws IOException {
		final MessageTemplateProvider templateProvider = new MessageTemplateProvider(appConfig);
		final MessageDataProvider messageDataProvider = new MessageDataProvider(templateProvider);
		final Config fbConfig = appConfig.getConfig("facebook");
		final PageAccessToken pageAccessToken = getPageAccessToken(fbConfig);
		return new FacebookPublisher(fbConfig, pageAccessToken, messageDataProvider);
	}

}