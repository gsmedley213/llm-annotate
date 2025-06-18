package io.github.gsmedley213.llmannotate.model.shared;

import java.util.List;

public record AnnotationJob(String bookPrefix, int annotationRun, String description, List<Notable> notables) {
}
