package com.jvm_bloggers.facebook.client.fb;

import com.jvm_bloggers.facebook.client.NewIssuePublishedData;
import com.jvm_bloggers.facebook.client.templates.MessageDataProvider;
import com.restfb.*;
import com.restfb.types.FacebookType;
import com.restfb.types.Post;
import com.typesafe.config.Config;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class FacebookPublisher {

    private final FacebookClient facebook;
    private final MessageDataProvider messageDataProvider;

    public FacebookPublisher(Config config,
                             PageAccessToken pageAccessToken,
                             MessageDataProvider messageDataProvider) throws IOException {
        this.messageDataProvider = messageDataProvider;
        String appSecret = config.getString("app.secret");
        facebook = new DefaultFacebookClient(pageAccessToken.getToken(), appSecret, Version.LATEST);
    }

    public String publishPost(NewIssuePublishedData issue) {
        final String messageData = messageDataProvider.prepareMessageData(issue);
        final FacebookType publishResponse =
            facebook.publish("me/feed", Post.class,
                Parameter.with("message", messageData),
                Parameter.with("link", issue.getUrl()));
        log.info("Message published with id {}", publishResponse.getId());
        return publishResponse.getId();
    }
}
