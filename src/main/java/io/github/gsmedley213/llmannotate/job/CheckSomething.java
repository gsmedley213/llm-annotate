package io.github.gsmedley213.llmannotate.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CheckSomething implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        log.info("Hello world!");
    }
}
