package io.github.gsmedley213.llmannotate.model.shared;

import java.util.List;

// TODO Copied from content-extractor. Better way to handle shared entities
public record AnnotationJob(String bookPrefix, int annotationRun, String description, List<Notable> notables) {
}
