package com.jvm_bloggers.facebook.client.kafka;

import akka.actor.ActorSystem;
import akka.kafka.ConsumerSettings;
import akka.kafka.Subscription;
import akka.kafka.Subscriptions;
import akka.kafka.javadsl.Consumer;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.Sink;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG;

@Slf4j
public class KafkaConsumer {

    public void run() {
        final ActorSystem system = ActorSystem.create();
        final Materializer materializer = ActorMaterializer.create(system);
        final Config config = ConfigFactory.defaultApplication();

        ConsumerSettings consumerSettings = settings(system, config);
        Subscription subscription = Subscriptions.topics(consumerSettings.getProperty("topic.issuePublished"));
        Sink loggingSink = Sink.foreach(obj -> log.info("Processed a message... {}", obj));

        Consumer
            .committableSource(consumerSettings, subscription)
            .map(msg -> {
                log.info("Received a message... {}", msg);
                return "etwas";
            })
            .runWith(loggingSink, materializer);
    }

    private ConsumerSettings settings(ActorSystem system, Config config) {
        final Config kafkaConfig = config.getConfig("kafka");
        return ConsumerSettings.apply(system, new ByteArrayDeserializer(), new StringDeserializer())
            .withBootstrapServers(kafkaConfig.getString("address"))
            .withGroupId(kafkaConfig.getString("group"))
            .withProperty(AUTO_OFFSET_RESET_CONFIG, "earliest");
    }

}
