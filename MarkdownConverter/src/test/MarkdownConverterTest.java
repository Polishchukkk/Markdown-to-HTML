
import main.MarkdownConverter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.*;
        import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class MarkdownConverterTest {

    private static final String TEST_INPUT_FILE = "testInput.md";
    private static final String TEST_OUTPUT_FILE = "testOutput.md";
    private static final String SAMPLE_MARKDOWN = "**bold** _italic_ `code` ```preformatted```";

    @BeforeEach
    public void setUp() throws IOException {
        // Create a sample input file for testing
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_INPUT_FILE))) {
            writer.write(SAMPLE_MARKDOWN);
        }
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(TEST_INPUT_FILE));
        Files.deleteIfExists(Paths.get(TEST_OUTPUT_FILE));
    }

    @Test
    public void testConvertToHtml() throws MarkdownConverter.InvalidMarkdownException {
        String markdownText = "**bold** _italic_ `code` ```preformatted```";
        String expectedHtml = "<b>bold</b> <i>italic</i> <tt>code</tt> <pre>preformatted</pre>";

        String html = MarkdownConverter.convertToHtml(markdownText);

        assertEquals(expectedHtml, html);
    }

    @Test
    public void testConvertToANSI() throws MarkdownConverter.InvalidMarkdownException {
        String markdownText = "**bold** _italic_ `code` ```preformatted```";
        String expectedANSI = "\u001B[1m" + "bold" + "\u001B[22m" + " " +
                "\u001B[3m" + "italic" + "\u001B[23m" + " " +
                "\u001B[7m" + "code" + "\u001B[27m" + " " +
                "\u001B[7m" + "preformatted" + "\u001B[27m";

        String ansi = MarkdownConverter.convertToANSI(markdownText);

        assertEquals(expectedANSI, ansi);
    }

    @Test
    public void testWriteToFile() throws IOException, MarkdownConverter.InvalidMarkdownException {
        String outputText = "Sample output text";
        MarkdownConverter.writeToFile(outputText, TEST_OUTPUT_FILE, null);

        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(TEST_OUTPUT_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        assertEquals(outputText, sb.toString());
    }
}
