package io.github.gsmedley213.llmannotate.job;

import io.github.gsmedley213.llmannotate.service.DebugService;
import io.github.gsmedley213.llmannotate.service.DevelopService;
import io.github.gsmedley213.llmannotate.service.impl.GeminiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
public class CheckSomething implements CommandLineRunner {

    @Autowired
    DebugService debugService;

    @Autowired
    GeminiService llmService;

    @Autowired
    DevelopService developService;

    @Override
    public void run(String... args) throws Exception {
        int run = Arrays.stream(args)
                .filter(arg -> arg.startsWith("run:"))
                .findFirst()
                .map(arg -> Integer.parseInt(arg.split(":")[1]))
                .orElse(1);
        developService.localRun(DevelopService.Book.LIVESTOCK_AND_ARMOUR, run);
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
