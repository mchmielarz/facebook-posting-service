package pl.devthoughts.facebook.client.fb;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Version;
import com.restfb.types.FacebookType;
import com.restfb.types.Post;
import com.typesafe.config.Config;
import lombok.extern.slf4j.Slf4j;
import pl.devthoughts.facebook.client.PostData;

import java.io.IOException;

@Slf4j
public class FacebookPublisher {

    private final FacebookClient facebook;

    public FacebookPublisher(Config config,
                             PageAccessToken pageAccessToken) throws IOException {
        String appSecret = config.getString("app.secret");
        facebook = new DefaultFacebookClient(pageAccessToken.getToken(), appSecret, Version.LATEST);
    }

    public String publishPost(PostData postData) {
        final FacebookType publishResponse =
            facebook.publish("me/feed", Post.class, postData.getParams());
        log.info("Message published with id {}", publishResponse.getId());
        return publishResponse.getId();
    }
}
