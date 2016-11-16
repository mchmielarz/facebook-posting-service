package com.jvm_bloggers.facebook.client.fb;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class PageAccessToken {

    private String access_token;

    public String getToken() {
        return access_token;
    }
}
