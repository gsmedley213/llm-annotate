package io.github.gsmedley213.llmannotate.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.gsmedley213.llmannotate.service.DebugService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Service
public
class DebugServiceImpl implements DebugService {

    @Autowired
    ObjectMapper mapper;

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

    @Override
    @SneakyThrows
    public String exampleText() {
        Resource resource = new ClassPathResource(("pnp_1.txt"));
        InputStream inputStream = resource.getInputStream();
        return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }
}
