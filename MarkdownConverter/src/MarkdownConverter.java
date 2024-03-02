
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MarkdownConverter {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: MarkdownConverter <input_file> [--out <output_file>]");
            System.exit(1);
        }

        String inputFile = args[0];
        String outputFile = null;

        // Перевірка, чи вказано вихідний файл
        if (args.length > 2 && args[1].equals("--out")) {
            outputFile = args[2];
        }

        try {
            String markdownText = readMarkdownFile(inputFile);
            String htmlText = convertToHtml(markdownText);
            if (outputFile != null) {
                writeHtmlToFile(htmlText, outputFile);
            } else {
                System.out.println(htmlText);
            }
        } catch (IOException e) {
            System.err.println("Error reading or writing file: " + e.getMessage());
            System.exit(1);
        } catch (InvalidMarkdownException e) {
            System.err.println("Error: invalid markdown " + e.getMessage());
            System.exit(1);
        }
    }

    private static String readMarkdownFile(String inputFile) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString();
    }

    private static void writeHtmlToFile(String htmlText, String outputFile) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            writer.write(htmlText);
        }
    }

    private static String convertToHtml(String markdownText) throws InvalidMarkdownException {


        // Обробка жирного тексту
        markdownText = markdownText.replaceAll("\\*\\*(.*?)\\*\\*", "<b>$1</b>");
        // Обробка курсивного тексту
        markdownText = markdownText.replaceAll("\\_(.*?)\\_", "<i>$1</i>");
        // Обробка преформатованого тексту
        markdownText = markdownText.replaceAll("(?s)```(.*?)```", "<pre>$1</pre>");
        // Обробка моноширинного тексту
        markdownText = markdownText.replaceAll("\\`(.*?)\\`", "<tt>$1</tt>");
        // Обробка параграфів
        markdownText = markdownText.replaceAll("(?m)\\n\\s*\\n|\\n{2,}", "</p>\n\n<p>");

        return markdownText;
    }

    static class InvalidMarkdownException extends Exception {
        public InvalidMarkdownException(String message) {
            super(message);
        }
    }
}
