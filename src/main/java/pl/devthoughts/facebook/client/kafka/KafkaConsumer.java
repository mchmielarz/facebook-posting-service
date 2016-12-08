package pl.devthoughts.facebook.client.kafka;

import akka.actor.ActorSystem;
import akka.kafka.ConsumerMessage;
import akka.kafka.ConsumerSettings;
import akka.kafka.Subscription;
import akka.kafka.Subscriptions;
import akka.kafka.javadsl.Consumer;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.Sink;
import lombok.extern.slf4j.Slf4j;
import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.RetryPolicy;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import pl.devthoughts.facebook.client.PostData;
import pl.devthoughts.facebook.client.fb.FacebookPublisher;

@Slf4j
public class KafkaConsumer {

    private final RetryPolicy retryPolicy = new RetryPolicy().retryOn(Exception.class);

    private final KafkaMessageParser mapper = new KafkaMessageParser();
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
                final PostData data = mapper.parse((String) committableMessage.record().value());
                final String postId = publishPost(data);
                committableMessage.committableOffset().commitJavadsl();
                return postId;
            })
            .runWith(loggingSink, materializer);
    }

    private String publishPost(PostData data) {
        return Failsafe.with(retryPolicy).get(() -> facebookPublisher.publishPost(data));
    }

}
