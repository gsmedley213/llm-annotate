# llm-annotate

**Part of the [Gutenberg Annotation Pipeline](#project-context).**

`llm-annotate` is a Java service that consumes extracted content from Project Gutenberg HTML files, uses an LLM (for now Google Gemini) to generate notes, and outputs the results as a JSON notes file.

---

## Project Context

This repository is one stage of a multi-step process for creating annotated versions of public domain books:

1. **content-extractor**  
   Extracts content from Project Gutenberg HTML and saves a new HTML file with elements marked so it can be associated with its content later.

2. **llm-annotate**  
   Consumes extracted content and uses a language model (Google Gemini for now) to generate notes for it, saving results as a notes JSON file.

3. **html-annotate**  
   Combines the marked HTML and notes JSON file to produce a version of the book with in-line annotations.

---

## Requirements

- Java 17+
- Gradle
- A Google Gemini account and API token ([Get started](https://aistudio.google.com/app/apikey))
- Extracted content from [content-extractor](https://github.com/gsmedley213/content-extractor)

---

## Configuration

1. **Set up your Gemini API token**

   Obtain a token from [Gemini API Keys](https://aistudio.google.com/app/apikey) and set it as an environment variable:

   ```sh
   export GEMINI_TOKEN=your_token_here
   ```

2. **Create/modify your `application.properties` file**

   Copy `application.example.properties` to `application.properties` and edit the values:

   ```properties
   # Path to root shared directory. Each book will have its own directory under this.
   shared.directory=../shared/books
   ```

   > **Note:** Unlike other stages, `llm-annotate` does not require you to specify the book's prefix or description in the properties file. These are automatically looked up from the `AnnotationJob` file in the shared directory.

3. **.gitignore is set up to ignore `application.properties`**  
   Do not commit your secrets or personal configuration.

---

## Running

```sh
./gradlew bootRun --args="--runLocal --directory=livestock_and_armour --run=1"
```

- `--runLocal`: Run in local development mode.
- `--directory`: Subdirectory for the book.
- `--run`: Run number for the run we are continuing.

On first run, `llm-annotate` will generate notes for all content; if interrupted, it will resume where it left off. If you wish to start fresh, delete the Notes file for this run in the book directory.

---

## Output

- Notes JSON files will be saved in the same directory as the extracted content.
- These notes can be combined with annotated HTML using [html-annotate](https://github.com/gsmedley213/html-annotate).

---

## Troubleshooting

- **Missing GEMINI_TOKEN**: Make sure the environment variable is set before running.
- **API errors**: Check your Gemini API quota and credentials.
- **Properties file errors**: Ensure your `application.properties` matches the expected format.

---

## License

This project is licensed under the [MIT License](LICENSE).

## See also

- [content-extractor](https://github.com/gsmedley213/content-extractor)
- [html-annotate](https://github.com/gsmedley213/html-annotate)