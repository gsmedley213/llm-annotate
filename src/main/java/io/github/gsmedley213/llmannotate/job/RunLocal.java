package io.github.gsmedley213.llmannotate.job;

import io.github.gsmedley213.llmannotate.service.DebugService;
import io.github.gsmedley213.llmannotate.service.DevelopService;
import io.github.gsmedley213.llmannotate.service.impl.GeminiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
public class RunLocal implements CommandLineRunner {

    @Autowired
    DebugService debugService;

    @Autowired
    GeminiService llmService;

    @Autowired
    DevelopService developService;

    @Value("${directory:}")
    private String directory;

    @Value("${run:-1}")
    private int run;

    @Override
    public void run(String... args) throws Exception {
        if (directory == null || directory.isBlank()) {
            throw new IllegalArgumentException("Missing required argument: --directory");
        }

        if (run == -1) {
            throw new IllegalArgumentException("Missing required argument: --run");
        }

        developService.localRun(directory, run);
    }

    private void basicQuestionExample(String... args) {
        String question = "Create a list of the words or concepts in the following text that would be above a fifth " +
                "grade reading level: ";

        String q2 = "For every word or concept in the following text that would be above a 5th grade reading level provide " +
                "an explanation of the word or concepts meaning that should be understandable to a 5th grader: "
                + debugService.exampleText();
        if (Arrays.asList(args).contains("ask")) {
            log.info("Asking {}: {}" , q2, llmService.ask(q2));
        } else if (Arrays.asList(args).contains("check")) {
            llmService.checkSomething(q2);
        }
    }
}
