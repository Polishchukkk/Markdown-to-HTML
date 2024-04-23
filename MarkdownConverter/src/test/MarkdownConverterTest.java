
import main.MarkdownConverter;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class MarkdownConverterTest {

    @Test
    public void testConvertToHtml_Bold() throws MarkdownConverter.InvalidMarkdownException {
        String input = "**bold**";
        String expectedOutput = "<b>bold</b>";
        String actualOutput = MarkdownConverter.convertToHtml(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testConvertToANSI_Bold() throws MarkdownConverter.InvalidMarkdownException {
        String input = "**bold**";
        String expectedOutput = "\u001B[1mbold\u001B[22m";
        String actualOutput = MarkdownConverter.convertToANSI(input);
        assertEquals(expectedOutput, actualOutput);
    }
}