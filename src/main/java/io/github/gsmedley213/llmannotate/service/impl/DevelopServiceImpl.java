package io.github.gsmedley213.llmannotate.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.gsmedley213.llmannotate.model.shared.AnnotationJob;
import io.github.gsmedley213.llmannotate.model.shared.ElementNotes;
import io.github.gsmedley213.llmannotate.model.shared.Notable;
import io.github.gsmedley213.llmannotate.model.shared.Note;
import io.github.gsmedley213.llmannotate.service.DevelopService;
import io.github.gsmedley213.llmannotate.service.LlmService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public
class DevelopServiceImpl implements DevelopService {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    LlmService llmService;

    @Override
    @SneakyThrows
    public void localRun(Book book, int run) {
        AnnotationJob job = mapper.readValue(book.sharedFolder(String.format("AnnotationJob-%s-%d.json",
                book.prefix, run)).toFile(), AnnotationJob.class);

        List<ElementNotes> elementNotes = new ArrayList<>();
        try {
            for (Notable notable : job.notables()) {
                List<Note> notes = llmService.generateNotes(book.description, notable.text());
                log.info("Generated {} notes.", notes.size());
                elementNotes.add(new ElementNotes(notable.elementId(), notes));
            }
        } catch (Exception e) {
            log.error("Exception while generating notes. Writing what we have so far.", e);
        }

        Files.writeString(book.sharedFolder(String.format("Notes-%s-%d.json", book.getPrefix(), run)),
                mapper.writeValueAsString(elementNotes));

        log.info("Got a job: {}", job.bookPrefix());
    }
}
