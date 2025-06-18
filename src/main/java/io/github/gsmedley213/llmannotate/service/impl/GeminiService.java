package io.github.gsmedley213.llmannotate.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.gsmedley213.llmannotate.model.gemini.GenerationConfig;
import io.github.gsmedley213.llmannotate.model.gemini.RequestBody;
import io.github.gsmedley213.llmannotate.model.gemini.Response;
import io.github.gsmedley213.llmannotate.model.shared.Note;
import io.github.gsmedley213.llmannotate.model.gemini.GeminiModel;
import io.github.gsmedley213.llmannotate.service.DebugService;
import io.github.gsmedley213.llmannotate.service.LlmService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.BiFunction;

@Slf4j
@Service
public class GeminiService implements LlmService {

    @Value("#{environment.GEMINI_TOKEN}")
    private final String geminiToken;

    private final DebugService debugService;

    private final ObjectMapper mapper;

    private final GeminiModel model;

    private final RestClient client;

    Queue<Instant> requestTimes = new LinkedList<>();

    public GeminiService(@Value("#{environment.GEMINI_TOKEN}") String geminiToken, DebugService debugService,
                         ObjectMapper mapper, GeminiModel model, RestClient client) {
        this.geminiToken = geminiToken;
        this.debugService = debugService;
        this.mapper = mapper;
        this.model = model;
        this.client = client;
    }

    @Override
    public String ask(String question) {
        Response response = sendQuery(RequestBody.single(question));

        return response.textContent();
    }

    @Override
    @SneakyThrows
    public List<Note> generateNotes(String context, String text) {
        String query = NoteQuery.ADULT.generate.apply(context, text);

        Response response = sendQuery(RequestBody.withSchema(query, new GenerationConfig()));

        return mapper.readValue(response.textContent(), new TypeReference<>() { });
    }


    @SneakyThrows
    public void checkSomething(String question) {
        debugService.writeToJson("genConf2", new GenerationConfig());

        Response response = sendQuery(RequestBody.withSchema(question, new GenerationConfig()));

        List<Note> words = mapper.readValue(response.textContent(), new TypeReference<List<Note>>() { });

        log.info("Got here.");
    }

    private Response sendQuery(RequestBody question) {
        respectLimits();

        return client.post()
                .uri("https://generativelanguage.googleapis.com/v1beta/models/{model}:generateContent?key={key}",
                        model.apiModel, geminiToken)
                .body(question)
                .retrieve()
                .body(Response.class);
    }

    private void respectLimits() {
        while (!requestTimes.isEmpty()
                && requestTimes.peek().isBefore(Instant.now().minusSeconds(60))) {
            requestTimes.poll();
        }

        if (requestTimes.size() >= model.requestsPerMinute) {
            try {
                log.info("Requests size {}, oldest request made at {}, sleeping for {} seconds.", requestTimes.size(),
                        requestTimes.peek(),
                        Duration.between(Instant.now(), requestTimes.peek().plusSeconds(61)).toSeconds());
                Thread.sleep(Duration.between(Instant.now(), requestTimes.peek().plusSeconds(61)));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            requestTimes.poll();
        }

        requestTimes.add(Instant.now());
    }

    private String noteQuery(String context, String text, int queryId) {
        List<String> queryFormats = Arrays.asList(
                "This is an excerpt from %s For every word or concept in the following text that would be above a 5th " +
                        "grade reading level provide an explanation of the word or concepts meaning that should be " +
                        "understandable to a 5th grader: %s"
        );
        return String.format(queryFormats.get(queryId), context, text);
    }

    enum NoteQuery {
        FIFTH_GRADE((context, text) -> String.format("This is an excerpt from %s For every word or concept in the following text that would be above a 5th " +
                "grade reading level provide an explanation of the word or concepts meaning that should be " +
                "understandable to a 5th grader: %s", context, text)),
        ADULT((context, text) -> String.format("This is an excerpt from %s Assume a reader who is an adult but with " +
                "limited knowledge of this historical period. For every word or concept that might be unknown to this " +
                "audience provide an explanation. Here is the excerpt: %s", context, text)),
        JUST_TEXT((context, text) -> String.format("For every word or concept in the following text that would be above a 5th " +
                "grade reading level provide an explanation of the word or concepts meaning that should be " +
                "understandable to a 5th grader: %s", text));

        public final BiFunction<String, String, String> generate;

        NoteQuery(BiFunction<String, String, String> generate) {
            this.generate = generate;
        }
    }
}
