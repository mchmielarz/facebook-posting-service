package pl.devthoughts.facebook.client.kafka

import com.github.charithe.kafka.EphemeralKafkaBroker
import com.github.charithe.kafka.KafkaJunitRule
import groovy.util.logging.Slf4j
import org.junit.Rule
import pl.devthoughts.facebook.client.fb.FacebookPublisher
import spock.lang.Specification
import spock.util.concurrent.BlockingVariable

@Slf4j
class KafkaSpec extends Specification {

    @Rule
    public KafkaJunitRule kafkaRule = new KafkaJunitRule(EphemeralKafkaBroker.create(9092, 2181));

    FacebookPublisher facebookPublisher = Mock(FacebookPublisher)

    private static final String MESSAGE = '{"link":"http://some.url","message":"Readme"}'

    def "Should consume a message from Kafka and publish it with Facebook publisher"() {
        given:
            def facebookAck = new BlockingVariable<Boolean>(1)
            facebookPublisher.publishPost(_) >> { arguments ->
                log.info("Issue data captured: " + arguments[0])
                facebookAck.set(true)
            }
        and:
            new KafkaConsumer(facebookPublisher).run()
        when:
            kafkaRule.helper().produceStrings("com.jvm_bloggers.issue.published", MESSAGE)
        then:
            facebookAck.get()
    }

}
