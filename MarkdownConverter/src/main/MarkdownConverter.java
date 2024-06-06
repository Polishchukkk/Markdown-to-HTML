package main;

import java.io.*;

public class MarkdownConverter {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: MarkdownConverter <input_file> [--out <output_file>] [--format <output_format>]");
            System.exit(1);
        }

        String inputFile = null;
        String outputFile = null;
        String outputFormat = null;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--out") && i + 1 < args.length) {
                outputFile = args[i + 1];
                i++;
            } else if (args[i].equals("--format") && i + 1 < args.length) {
                outputFormat = args[i + 1];
                i++;
            } else {
                inputFile = args[i];
            }
        }

        if (inputFile == null) {
            System.err.println("Input file is missing.");
            System.exit(1);
        }

        try {
            String markdownText = readMarkdownFile(inputFile);
            String outputText = convertToFormat(markdownText, outputFormat);
            if (outputFile != null) {
                writeToFile(outputText, outputFile, outputFormat);
            } else {
                if ("ansi".equalsIgnoreCase(outputFormat)) {
                    printFormattedText(outputText, true);
                } else {
                    System.out.println(outputText);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading or writing file: " + e.getMessage());
            System.exit(1);
        } catch (InvalidMarkdownException e) {
            System.err.println("Error: invalid markdown " + e.getMessage());
            System.exit(1);
        }
    }

    public static String readMarkdownFile(String inputFile) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString();
    }

    public static void writeToFile(String outputText, String outputFile, String outputFormat) throws IOException {
        if ("ansi".equalsIgnoreCase(outputFormat)) {
            try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(outputFile)))) {
                printFormattedText(outputText, false, writer);
            }
        } else {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
                writer.write(outputText);
            }
        }
    }

    private static String convertToFormat(String markdownText, String outputFormat) throws InvalidMarkdownException {
        if ("ansi".equalsIgnoreCase(outputFormat)) {
            markdownText = convertToANSI(markdownText);
        } else if ("html".equalsIgnoreCase(outputFormat)) {
            markdownText = convertToHtml(markdownText);
        }
        return markdownText;
    }

    public static String convertToHtml(String markdownText) throws InvalidMarkdownException {
        markdownText = markdownText.replaceAll("\\*\\*(.*?)\\*\\*", "<b>$1</b>");
        markdownText = markdownText.replaceAll("\\_(.*?)\\_", "<i>$1</i>");
        markdownText = markdownText.replaceAll("(?s)```(.*?)```", "<pre>$1</pre>");
        markdownText = markdownText.replaceAll("\\`(.*?)\\`", "<tt>$1</tt>");
        markdownText = markdownText.replaceAll("(?m)\\n\\s*\\n|\\n{2,}", "</p>\n\n<p>");
        return markdownText;
    }

    public static String convertToANSI(String markdownText) throws InvalidMarkdownException {
        markdownText = markdownText.replaceAll("\\*\\*(.*?)\\*\\*", "\u001B[1m$1\u001B[22m");
        markdownText = markdownText.replaceAll("\\_(.*?)\\_", "\u001B[3m$1\u001B[23m");
        markdownText = markdownText.replaceAll("(?s)```(.*?)```", "\u001B[7m$1\u001B[27m");
        markdownText = markdownText.replaceAll("\\`(.*?)\\`", "\u001B[7m$1\u001B[27m");
        return markdownText;
    }

    private static void printFormattedText(String outputText, boolean inverted) {
        printFormattedText(outputText, inverted, new PrintWriter(System.out));
    }

    private static void printFormattedText(String outputText, boolean inverted, PrintWriter writer) {
        writer.println(outputText);
        writer.flush();
    }

    public static class InvalidMarkdownException extends Exception {
        public InvalidMarkdownException(String message) {
            super(message);
        }
    }
}