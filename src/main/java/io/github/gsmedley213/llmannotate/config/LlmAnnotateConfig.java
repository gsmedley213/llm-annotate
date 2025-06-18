package io.github.gsmedley213.llmannotate.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.gsmedley213.llmannotate.model.gemini.GeminiModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;


@Configuration
public class LlmAnnotateConfig {

    @Bean
    ObjectMapper mapper() {
        return new ObjectMapper();
    }

    @Bean
    RestClient restClient() {
        return RestClient.create();
    }

    @Bean
    GeminiModel geminiModel() { return GeminiModel.FLASH_2; }
}
