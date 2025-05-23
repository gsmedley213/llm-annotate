package io.github.gsmedley213.llmannotate.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.gsmedley213.llmannotate.gemini.model.GenerationConfig;
import io.github.gsmedley213.llmannotate.gemini.model.RequestBody;
import io.github.gsmedley213.llmannotate.gemini.model.Response;
import io.github.gsmedley213.llmannotate.model.Lexy;
import io.github.gsmedley213.llmannotate.service.DebugService;
import io.github.gsmedley213.llmannotate.service.LlmService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Slf4j
@Service
public class GeminiService implements LlmService {

    @Value("#{environment.GEMINI_TOKEN}")
    private String geminiToken;

    @Autowired
    DebugService debugService;

    @Autowired
    ObjectMapper mapper;

    @Override
    public String ask(String question) {
        RestClient client = RestClient.create();

        Response response = client.post()
                .uri("https://generativelanguage.googleapis.com/v1beta/models/{model}:generateContent?key={key}",
                        "gemini-2.0-flash", geminiToken)
                .body(RequestBody.single(question))
                .retrieve()
                .body(Response.class);

        return response.textContent();
    }

    @SneakyThrows
    public void checkSomething(String question) {
        RestClient client = RestClient.create();

        debugService.writeToJson("genConf2", new GenerationConfig());

        Response response = client.post()
                .uri("https://generativelanguage.googleapis.com/v1beta/models/{model}:generateContent?key={key}",
                        "gemini-2.0-flash", geminiToken)
                .body(RequestBody.withSchema(question, new GenerationConfig()))
                .retrieve()
                .body(Response.class);

        List<Lexy> words = mapper.readValue(response.textContent(), new TypeReference<List<Lexy>>() { });

        log.info("Got here.");
    }
}
