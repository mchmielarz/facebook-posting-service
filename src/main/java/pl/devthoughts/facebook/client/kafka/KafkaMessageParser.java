package pl.devthoughts.facebook.client.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restfb.Parameter;
import pl.devthoughts.facebook.client.PostData;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class KafkaMessageParser {

    private final ObjectMapper mapper = new ObjectMapper();

    PostData parse(String message) throws IOException {
        final Map<String, Object> map = mapper.readValue(message, HashMap.class);
        final List<Parameter> parameters = map.entrySet()
            .stream()
            .map(entry -> Parameter.with(entry.getKey(), entry.getValue()))
            .collect(toList());
        return new PostData(parameters);
    }

}
