package io.extremum.functions.doc.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.extremum.mapper.jackson.BasicJsonObjectMapper;
import java.nio.charset.StandardCharsets;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;

class YamlMessageConverter extends AbstractJackson2HttpMessageConverter {

    public YamlMessageConverter() {
        super(getMapper(),
                new MediaType("application", "yaml", StandardCharsets.UTF_8),
                new MediaType("text", "yaml", StandardCharsets.UTF_8),
                new MediaType("application", "*+yaml", StandardCharsets.UTF_8),
                new MediaType("text", "*+yaml", StandardCharsets.UTF_8),
                new MediaType("application", "yml", StandardCharsets.UTF_8),
                new MediaType("text", "yml", StandardCharsets.UTF_8),
                new MediaType("application", "*+yaml", StandardCharsets.UTF_8),
                new MediaType("text", "*+yaml", StandardCharsets.UTF_8));
    }

    private static ObjectMapper getMapper() {
        return new BasicJsonObjectMapper(new YAMLFactory());
    }

    @Override
    public void setObjectMapper(final ObjectMapper objectMapper) {
        if (!(objectMapper.getFactory() instanceof YAMLFactory)) {
            throw new IllegalArgumentException(
                    "ObjectMapper must be configured with an instance of YAMLFactory");
        }

        super.setObjectMapper(objectMapper);
    }
}
