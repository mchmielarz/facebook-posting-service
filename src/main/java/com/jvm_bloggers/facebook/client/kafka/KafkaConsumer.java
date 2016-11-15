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
import com.jvm_bloggers.facebook.client.fb.FacebookPublisher;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

@Slf4j
public class KafkaConsumer {

    private final ObjectMapper mapper = new ObjectMapper();
    private final FacebookPublisher facebookPublisher;

    public KafkaConsumer(FacebookPublisher facebookPublisher) {
        this.facebookPublisher = facebookPublisher;
    }

    public void run() {
        final ActorSystem system = ActorSystem.create();
        final Materializer materializer = ActorMaterializer.create(system);

        ConsumerSettings consumerSettings = ConsumerSettings.apply(system, new ByteArrayDeserializer(), new StringDeserializer());
        Subscription subscription = Subscriptions.topics(consumerSettings.getProperty("topic.issuePublished"));
        Sink loggingSink = Sink.foreach(obj -> log.info("Processed a message... {}", obj));

        Consumer
            .committableSource(consumerSettings, subscription)
            .map(msg -> {
                log.info("Received a message: {}", msg);
                ConsumerMessage.CommittableMessage committableMessage = (ConsumerMessage.CommittableMessage) msg;
                final NewIssuePublishedData data = parse(committableMessage);
                final String postId = facebookPublisher.publishPost(data);
                committableMessage.committableOffset().commitJavadsl();
                return postId;
            })
            .runWith(loggingSink, materializer);
    }

    private NewIssuePublishedData parse(ConsumerMessage.CommittableMessage msg) throws java.io.IOException {
        final Object value = msg.record().value();
        return mapper.readValue((String) value, NewIssuePublishedData.class);
    }

}
