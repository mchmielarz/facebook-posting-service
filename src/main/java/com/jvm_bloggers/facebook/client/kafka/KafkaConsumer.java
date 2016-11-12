package com.jvm_bloggers.facebook.client.kafka;

import akka.actor.ActorSystem;
import akka.kafka.ConsumerMessage;
import akka.kafka.ConsumerSettings;
import akka.kafka.Subscription;
import akka.kafka.Subscriptions;
import akka.kafka.javadsl.Consumer;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.Sink;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jvm_bloggers.facebook.client.NewIssuePublishedData;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG;

@Slf4j
public class KafkaConsumer {

    private final ObjectMapper mapper = new ObjectMapper();

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
                log.info("Received a message: {}", msg);
                ConsumerMessage.CommittableMessage committableMessage = (ConsumerMessage.CommittableMessage) msg;
                final NewIssuePublishedData data = parse(committableMessage);
                log.info("Parsed issue data: {}", data);
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

    private NewIssuePublishedData parse(ConsumerMessage.CommittableMessage msg) throws java.io.IOException {
        final Object value = msg.record().value();
        return mapper.readValue((String) value, NewIssuePublishedData.class);
    }

}
