package io.github.gsmedley213.llmannotate.job;

import io.github.gsmedley213.llmannotate.gemini.model.RequestBody;
import io.github.gsmedley213.llmannotate.gemini.model.Response;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import org.springframework.web.client.RestClient;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class CheckSomething implements CommandLineRunner {

    @Value("#{environment.GEMINI_TOKEN}")
    private String geminiToken;

    @Override
    public void run(String... args) throws Exception {
        String question = "Create a list of the words or concepts in the following text that would be above a fifth " +
                "grade reading level: ";
        log.info("{}: {}", question, queryLlm(question + firstChapter()));
    }

    private String queryLlm(String question) {
        RestClient client = RestClient.create();
        Response response = client.post()
                .uri("https://generativelanguage.googleapis.com/v1beta/models/{model}:generateContent?key={key}",
                        "gemini-2.0-flash", geminiToken)
                .body(RequestBody.single(question))
                .retrieve()
                .body(Response.class);

        return response.candidates().getFirst().content().parts().getFirst().text();
    }

    @SneakyThrows
    String firstChapter() {
        Resource resource = new ClassPathResource(("pnp_1.txt"));
        InputStream inputStream = resource.getInputStream();
        return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }

}
