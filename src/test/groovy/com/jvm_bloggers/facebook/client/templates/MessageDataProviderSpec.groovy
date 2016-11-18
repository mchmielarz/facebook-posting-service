package com.jvm_bloggers.facebook.client.templates

import com.jvm_bloggers.facebook.client.NewIssuePublishedData
import spock.lang.Specification

class MessageDataProviderSpec extends Specification {

    private static final String ISSUE_URL = 'http://jvm-bloggers.com'

    def "Should prepare a Facebook message based on an issue data"() {
        given:
            NewIssuePublishedData data = new NewIssuePublishedData(123L, ISSUE_URL)
        and:
            MessageTemplateProvider templateProvider = Mock(MessageTemplateProvider)
            templateProvider.randomTemplate() >> 'Nowe, już <issueNumber> wydanie JVM Bloggers jest dostępne na stronie <url>'
        when:
            String messageData = new MessageDataProvider(templateProvider).prepareMessageData(data)
        then:
            messageData == "Nowe, już 123 wydanie JVM Bloggers jest dostępne na stronie $ISSUE_URL"
    }

}
