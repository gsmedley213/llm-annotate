package io.github.gsmedley213.llmannotate.model.gemini;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestBody {
    private List<Content> contents;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private GenerationConfig generationConfig;

    public static RequestBody single(String text) {
        return new RequestBody(Collections.singletonList(Content.single(text)), null);
    }

    public static RequestBody withSchema(String text, GenerationConfig config) {
        return new RequestBody(Collections.singletonList(Content.single(text)), config);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Content {
        private List<Part> parts;

        public static Content single(String text) {
            return new Content(Collections.singletonList(new Part(text)));
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Part {
        private String text;
    }
}

