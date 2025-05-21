package io.github.gsmedley213.llmannotate.job;

import io.github.gsmedley213.llmannotate.gemini.model.RequestBody;
import io.github.gsmedley213.llmannotate.gemini.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class CheckSomething implements CommandLineRunner {

    @Value("#{environment.GEMINI_TOKEN}")
    private String geminiToken;

    @Override
    public void run(String... args) throws Exception {
        RestClient client = RestClient.create();
        Response response = client.post()
                .uri("https://generativelanguage.googleapis.com/v1beta/models/{model}:generateContent?key={key}",
                        "gemini-2.0-flash", geminiToken)
                .body(RequestBody.single("Explain how AI works in a few words"))
                .retrieve()
                .body(Response.class);

        log.info(response.candidates().getFirst().content().parts().getFirst().text());
    }
}
