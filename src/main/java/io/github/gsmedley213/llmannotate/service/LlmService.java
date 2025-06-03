package io.github.gsmedley213.llmannotate.service;

import io.github.gsmedley213.llmannotate.model.shared.Note;

import java.util.List;

public interface LlmService {

    String ask(String question);

    List<Note> generateNotes(String context, String text);
}
