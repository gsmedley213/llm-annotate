package io.github.gsmedley213.llmannotate.gemini.model;

import java.util.Collections;
import java.util.List;

public record RequestBody (
        List<Content> contents
) {
    public static RequestBody single(String text) {
        return new RequestBody(Collections.singletonList(Content.single(text)));
    }

    public record Content (
            List<Part> parts
    ) {
        public static Content single(String text) {
            return new Content(Collections.singletonList(new Part(text)));
        }
    }

    public record Part(
            String text
    ) { }
}

