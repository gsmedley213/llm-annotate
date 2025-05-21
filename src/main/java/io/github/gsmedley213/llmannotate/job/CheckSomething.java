package io.github.gsmedley213.llmannotate.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class CheckSomething implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        RestClient client = RestClient.create();
        String message = client.get()
                .uri("https://mocktarget.apigee.net",
                        uriBuilder -> uriBuilder.queryParam("user", "world")
                                .build())
                .retrieve()
                .body(String.class);

        log.info(message);
    }
}
