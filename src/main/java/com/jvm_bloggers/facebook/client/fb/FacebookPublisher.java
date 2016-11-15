package com.jvm_bloggers.facebook.client.fb;

import com.jvm_bloggers.facebook.client.NewIssuePublishedData;
import com.typesafe.config.Config;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FacebookPublisher {

    public FacebookPublisher(Config config) {}

    public String publishPost(NewIssuePublishedData issue) {
        return "etwas";
    }
}
