package pl.devthoughts.facebook.client;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import pl.devthoughts.facebook.client.fb.FacebookPublisher;
import pl.devthoughts.facebook.client.fb.PageAccessToken;
import pl.devthoughts.facebook.client.fb.PageAccessTokenProvider;
import pl.devthoughts.facebook.client.kafka.KafkaConsumer;

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
		final Config fbConfig = appConfig.getConfig("facebook");
		final PageAccessToken pageAccessToken = getPageAccessToken(fbConfig);
		return new FacebookPublisher(fbConfig, pageAccessToken);
	}

}