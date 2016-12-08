package pl.devthoughts.facebook.client.kafka

import com.restfb.Parameter
import pl.devthoughts.facebook.client.PostData
import spock.lang.Specification

class KafkaMessageParserSpec extends Specification {

    def "Should parse a given message to a post data"() {
        given:
            String message = '{"link":"http://some.url","message":"Readme"}'
        when:
            PostData data = new KafkaMessageParser().parse(message)
        then:
            data.params.size() == 2
            data.params.contains(Parameter.with("link", "http://some.url"))
            data.params.contains(Parameter.with("message", "Readme"))
    }

}
