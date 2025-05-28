package io.github.gsmedley213.llmannotate.model.gemini;

public enum GeminiModel {
    FLASH_2("gemini-2.0-flash", 15, 1_000_000, 1_500);

    public final String apiModel;
    public final int requestsPerMinute;
    public final int tokensPerMinute;
    public final int requestsPerDay;

    GeminiModel(String apiModel, int requestsPerMinute, int tokensPerMinute, int requestsPerDay) {
        this.apiModel = apiModel;
        this.requestsPerMinute = requestsPerMinute;
        this.tokensPerMinute = tokensPerMinute;
        this.requestsPerDay = requestsPerDay;
    }
}
