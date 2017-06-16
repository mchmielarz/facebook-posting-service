package pl.devthoughts.facebook.client.fb;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.FacebookType;
import com.restfb.types.Post;
import com.typesafe.config.Config;

import lombok.extern.slf4j.Slf4j;

import pl.devthoughts.facebook.client.PostData;

import java.io.IOException;

import static com.restfb.Version.VERSION_2_9;

@Slf4j
public class FacebookPublisher {

    private final FacebookClient facebook;

    public FacebookPublisher(Config config,
                             PageAccessToken pageAccessToken) throws IOException {
        String appSecret = config.getString("app.secret");
        facebook = new DefaultFacebookClient(pageAccessToken.getToken(), appSecret, VERSION_2_9);
    }

    public String publishPost(PostData postData) {
        log.info("Sending a new message to Facebook: {}", postData);
        final FacebookType publishResponse =
            facebook.publish("me/feed", Post.class, postData.getParams());
        log.info("Message published with id {}", publishResponse.getId());
        return publishResponse.getId();
    }
}
