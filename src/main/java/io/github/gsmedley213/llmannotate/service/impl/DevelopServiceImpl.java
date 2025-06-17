package io.github.gsmedley213.llmannotate.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public
class DevelopServiceImpl implements DevelopService {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    LlmService llmService;

    @Value("${shared.directory}")
    String shared;

    @Override
    @SneakyThrows
    public void localRun(String directory, int run) {
        AnnotationJob job = findJob(directory, run);

        List<ElementNotes> elementNotes = getElementNotes(directory, job.bookPrefix(), run);
        if (!elementNotes.isEmpty()) {
            Set<Integer> annotatedElements = elementNotes.stream()
                    .map(ElementNotes::elementId)
                    .collect(Collectors.toSet());

            // If we are restarting a run where we left off, filter out notes we've already run.
            job = new AnnotationJob(job.bookPrefix(), job.annotationRun(), job.description(),
                job.notables()
                        .stream()
                        .filter(note -> !annotatedElements.contains(note.elementId()))
                        .toList()
            );
        }

        try {
            for (Notable notable : job.notables()) {
                List<Note> notes = llmService.generateNotes(job.description(), notable.text());
                // For short notes, the LLM may return notes on the description rather than on the text. Remove all notes
                // with a word or concept that is not found in the text
                notes = notes.stream()
                        .filter(note -> notable.text().toLowerCase().contains(note.wordOrConcept().toLowerCase()))
                        .toList();
                log.info("Generated {} notes.", notes.size());
                elementNotes.add(new ElementNotes(notable.elementId(), notes));
            }
        } catch (Exception e) {
            log.error("Exception while generating notes. Writing what we have so far.", e);
        }

        Files.writeString(Paths.get(shared, directory, String.format("Notes-%s-%d.json", job.bookPrefix(), run)),
                mapper.writeValueAsString(elementNotes));

        log.info("Annotation completed.");
    }

    private List<ElementNotes> getElementNotes(String directory, String prefix, int run) {
        Path jobDirectory = Paths.get(shared, directory);
        if (jobDirectory.toFile().isDirectory()) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(jobDirectory,
                    String.format("Notes-%s-%d.json", prefix, run))) {
                var notesFile = stream.iterator();
                if (notesFile.hasNext()) {
                    return new ArrayList<>(
                            mapper.readValue(notesFile.next().toFile(), new TypeReference<List<ElementNotes>>() {
                            })
                    );
                }
            } catch (IOException e) {
                log.warn("Exception while checking for in progress Notes file.", e);
            }
        }
        return new ArrayList<>();
    }

    private AnnotationJob findJob(String directory, int run) {
        Path jobDirectory = Paths.get(shared, directory);
        if (jobDirectory.toFile().isDirectory()) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(jobDirectory,
                    "AnnotationJob-*-" + run + ".json")) {
                var jobFiles = stream.iterator();
                if (jobFiles.hasNext()) {
                    Path first = jobFiles.next();

                    if (jobFiles.hasNext()) {
                        throw new IllegalStateException("Multiple AnnotationJob files found. Something has gone wrong.");
                    }

                    return mapper.readValue(first.toFile(), AnnotationJob.class);
                } else {
                    throw new IllegalStateException("Unable to find AnnotationJob file. This file must exist for this " +
                            "service to run.");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new IllegalArgumentException(String.format("'%s' is not a directory.", jobDirectory));
        }
    }
}
