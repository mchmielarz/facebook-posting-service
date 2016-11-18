package com.jvm_bloggers.facebook.client.templates

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import spock.lang.Specification

class MessageTemplateProviderSpec extends Specification {

    def "Should generate non-empty template to be filled with data"() {
        given:
            Config config = ConfigFactory.load()
        when:
            String template = new MessageTemplateProvider(config).randomTemplate()
        then:
            config.getStringList("newIssue.templates").contains(template)
    }

}
