package io.github.gsmedley213.llmannotate.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class LlmAnnotateConfig {

    @Bean
    ObjectMapper mapper() {
        return new ObjectMapper();
    }
}
