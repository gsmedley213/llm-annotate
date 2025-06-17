package io.github.gsmedley213.llmannotate.service;

public interface DebugService {

    void writeToFile(String filename, String content);

    void writeToJson(String filename, Object content);

    String exampleText();

}
