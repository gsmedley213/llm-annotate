package io.github.gsmedley213.llmannotate.service;

import lombok.Getter;

import java.nio.file.Path;
import java.nio.file.Paths;

public interface DevelopService {

    void localRun(Book book, int run);

    @Getter
    enum Book {
        GK_CHESTERTON("gk_chesterton", "pg27080",
                "\"G. K. Chesterton: A Critical Study\" by Julius West is a critical analysis written in the early 20th century. The book examines the life, work, and influence of British author G. K. Chesterton, highlighting his literary contributions and the impact of his ideas on contemporary thought. The focus is on Chesterton’s unique style, his approach to various literary genres, and his philosophical perspectives, particularly regarding religion and society."),
        GOLDEN_AGE("golden_age", "pg32501",
                "\"The Golden Age\" by Kenneth Grahame is a novel written in the late 19th century. It captures the nostalgic reflections of childhood, exploring themes of imagination, innocence, and the contrasting perspective of adults through the eyes of children."),
        LIVESTOCK_AND_ARMOUR("livestock_and_armour", "pg51244",
                "\"The Livestock Producer and Armour\" by Armour and Company is a scientific publication written in the early 20th century. This book discusses the dynamics of the livestock industry, the evolution of meat packing, and the economic relations between producers and packers, reflecting a time when the industry was adjusting to modern practices and market demands post-World War I."),
        NAPOLEON_1("napoleon_1", "pg48837",
                "\"Life of Napoleon Bonaparte, Volume I\" by Sir Walter Scott is a historical account written in the early 19th century. The narrative provides an in-depth examination of Napoleon's life amidst the backdrop of the French Revolution and the significant political upheavals of the time."),
        REJECTED_MEN("rejected_men", "pg46841",
                "\"Rejected of Men: A Story of To-day\" by Howard Pyle is a historical fiction novel written in the early 20th century. The narrative re-examines the biblical story of the crucifixion from the perspective of the scribes, Pharisees, priests, and Romans, offering a unique viewpoint that challenges contemporary interpretations of those events.");

        private final String directory;
        private final String prefix;
        private final String description; // Should be helpful context for an LLM

        Book(String directory, String prefix, String description) {
            this.directory = directory;
            this.prefix = prefix;
            this.description = description;
        }

        public Path sharedFolder() {
            return Paths.get("..", "shared", "books", directory);
        }

        public Path sharedFolder(String filename) {
            return Paths.get("..", "shared", "books", directory, filename);
        }
    }
}
