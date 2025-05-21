package io.github.gsmedley213.llmannotate.gemini.model;

import java.util.List;

public record Response(
        List<Candidate> candidates,
        UsageMetadata usageMetadata,
        String modelVersion
) {

    public record Candidate(
            Content content,
            String finishReason,
            double avgLogprobs
    ) {
    }

    public record Content(
            List<Part> parts,
            String role
    ) {
    }

    public record Part(
            String text
    ) {
    }

    public record UsageMetadata(
            int promptTokenCount,
            int candidatesTokenCount,
            int totalTokenCount,
            List<TokenDetail> promptTokensDetails,
            List<TokenDetail> candidatesTokensDetails
    ) {
    }

    public record TokenDetail(
            String modality,
            int tokenCount
    ) {
    }
}