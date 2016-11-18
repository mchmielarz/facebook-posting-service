package com.jvm_bloggers.facebook.client.templates;

import com.jvm_bloggers.facebook.client.NewIssuePublishedData;
import org.stringtemplate.v4.ST;

public class MessageDataProvider {

    private final MessageTemplateProvider templateProvider;

    public MessageDataProvider(MessageTemplateProvider templateProvider) {
        this.templateProvider = templateProvider;
    }

    public String prepareMessageData(NewIssuePublishedData issueData) {
        ST st = new ST(templateProvider.randomTemplate());
        st.add("issueNumber", issueData.getIssueNumber());
        st.add("url", issueData.getUrl());
        return st.render();
    }

}
