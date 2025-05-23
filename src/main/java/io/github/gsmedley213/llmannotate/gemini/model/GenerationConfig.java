package io.github.gsmedley213.llmannotate.gemini.model;

import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Data
public class GenerationConfig {
    String responseMimeType = "application/json";
    ResponseSchema responseSchema = new ResponseSchema();

    @Data
    public static class ResponseSchema {
        String type = "ARRAY";
        Items items = new Items();
    }

    @Data
    public static class Items {
        String type = "OBJECT";
        Map<String, StringType> properties = Map.of(
                "wordOrConcept", new StringType(),
                "explanation", new StringType()
                );
        List<String> propertyOrdering = Arrays.asList("wordOrConcept", "explanation");
    }

    @Data
    public static class StringType {
        String type = "STRING";
    }
}
