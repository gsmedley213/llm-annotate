package io.github.gsmedley213.llmannotate.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;

public interface DebugService {

    void writeToFile(String filename, String content);

    void writeToJson(String filename, Object content);

    @Slf4j
    @Service
    class DebugServiceImpl implements DebugService {

        private final ObjectMapper mapper = new ObjectMapper();

        @Override
        @SneakyThrows
        public void writeToFile(String filename, String content) {
            Files.writeString(Path.of("local", filename), content);
        }

        @Override
        @SneakyThrows
        public void writeToJson(String filename, Object content) {
            writeToFile(filename + ".json", mapper.writeValueAsString(content));
        }
    }
}
