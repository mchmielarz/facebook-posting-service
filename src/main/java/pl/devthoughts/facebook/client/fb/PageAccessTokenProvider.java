package pl.devthoughts.facebook.client.fb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restfb.DefaultFacebookClient;
import com.restfb.WebRequestor;
import com.typesafe.config.Config;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static com.restfb.Version.VERSION_2_9;

@Slf4j
public class PageAccessTokenProvider {

    private static final String PAGE_TOKEN_URL = "https://graph.facebook.com/%s?fields=access_token&access_token=%s&appsecret_proof=%s";

    private final DefaultFacebookClient facebookClient;
    private final String appSecret;
    private final String userAccessToken;
    private final String pageId;
    private final ObjectMapper objectMapper;

    public PageAccessTokenProvider(Config facebookConfig) {
        facebookClient = new DefaultFacebookClient(VERSION_2_9);
        appSecret = facebookConfig.getString("app.secret");
        userAccessToken = facebookConfig.getString("user.access.token");
        pageId = facebookConfig.getString("page.id");
        objectMapper = new ObjectMapper();
    }

    public PageAccessToken getToken() throws IOException {
        log.info("Fetching application secret proof...");
        String appSecretProof = facebookClient.obtainAppSecretProof(userAccessToken, appSecret);
        log.info("Requesting page access token...");
        WebRequestor.Response response = facebookClient.getWebRequestor().executeGet(
            String.format(PAGE_TOKEN_URL, pageId, userAccessToken, appSecretProof)
        );
        return extractToken(response);
    }

    private PageAccessToken extractToken(WebRequestor.Response response) throws IOException {
        return objectMapper
            .readValue(response.getBody(), PageAccessToken.class);
    }

}
