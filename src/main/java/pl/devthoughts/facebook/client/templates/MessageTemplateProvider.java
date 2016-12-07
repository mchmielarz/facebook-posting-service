package pl.devthoughts.facebook.client.templates;

import com.typesafe.config.Config;

import java.util.List;
import java.util.Random;

public class MessageTemplateProvider {

    private final List<String> templates;
    private final Random random = new Random();

    public MessageTemplateProvider(Config config) {
        templates = config.getStringList("newIssue.templates");
    }

    String randomTemplate() {
        return templates.get(random.nextInt(templates.size()));
    }

}
