package com.jvm_bloggers.facebook.client.fb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor(access = PRIVATE)
public class PageAccessToken {

    @JsonProperty("access_token")
    private String token;

    public String getToken() {
        return token;
    }
}
