package com.jvm_bloggers.facebook.client.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.charithe.kafka.EphemeralKafkaBroker
import com.github.charithe.kafka.KafkaJunitRule
import com.jvm_bloggers.facebook.client.NewIssuePublishedData
import com.jvm_bloggers.facebook.client.fb.FacebookPublisher
import groovy.util.logging.Slf4j
import org.junit.Rule
import spock.lang.Specification
import spock.util.concurrent.BlockingVariable

@Slf4j
class KafkaSpec extends Specification {

    @Rule
    public KafkaJunitRule kafkaRule = new KafkaJunitRule(EphemeralKafkaBroker.create(9092, 2181));

    FacebookPublisher facebookPublisher = Mock(FacebookPublisher)

    def "should do kafka"() {
        given:
            NewIssuePublishedData data = new NewIssuePublishedData(55, "http://jvm-bloggers.com/")
            String issueData = new ObjectMapper().writeValueAsString(data)
        and:
            def awaitTillTrue = new BlockingVariable<Boolean>(1)
            facebookPublisher.publishPost(_) >> {
                awaitTillTrue.set(true)
            }
        and:
            new KafkaConsumer(facebookPublisher).run()
        when:
            kafkaRule.helper().produceStrings("com.jvm_bloggers.issue.published", issueData)
        then:
            awaitTillTrue.get()
    }

}
