package com.jvm_bloggers.facebook.client.fb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restfb.DefaultFacebookClient;
import com.restfb.Version;
import com.restfb.WebRequestor;
import com.typesafe.config.Config;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class PageAccessTokenProvider {

    private static final String PAGE_TOKEN_URL = "https://graph.facebook.com/%s?fields=access_token&access_token=%s&appsecret_proof=%s";

    private final DefaultFacebookClient facebookClient;
    private final String appSecret;
    private final String accessToken;
    private final String pageId;
    private final ObjectMapper objectMapper;

    public PageAccessTokenProvider(Config facebookConfig) {
        facebookClient = new DefaultFacebookClient(Version.LATEST);
        appSecret = facebookConfig.getString("app.secret");
        accessToken = facebookConfig.getString("access.token");
        pageId = facebookConfig.getString("page.id");
        objectMapper = new ObjectMapper();
    }

    public PageAccessToken getToken() throws IOException {
        log.info("Fetching application secret proof...");
        String appSecretProof = facebookClient.obtainAppSecretProof(accessToken, appSecret);
        log.info("Requesting page access token...");
        WebRequestor.Response response = facebookClient.getWebRequestor().executeGet(
            String.format(PAGE_TOKEN_URL, pageId, accessToken, appSecretProof)
        );
        return extractToken(response);
    }

    private PageAccessToken extractToken(WebRequestor.Response response) throws IOException {
        return objectMapper
            .readValue(response.getBody(), PageAccessToken.class);
    }

}
