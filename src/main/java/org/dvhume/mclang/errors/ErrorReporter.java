package org.dvhume.mclang.errors;

/*
 * Created by DvHume in 24.08.2026.
*/

import org.dvhume.mclang.lexer.Token;

public class ErrorReporter {

    private final String sourceCode;
    private final String fileName;
    private final boolean ansSupport;

    public ErrorReporter(String sourceCode, String fileName) {
        this.sourceCode = sourceCode;
        this.fileName = fileName;
        this.ansSupport = checkAnsiSupport();
    }

    private boolean checkAnsiSupport() {
        String os = System.getProperty("os.name").toLowerCase();
        if (!os.contains("win")) {
            return true;
        }

        try {
            new ProcessBuilder("cmd", "/c", "echo \033[0m").inheritIO().start().waitFor();
            return true;
        } catch (Exception e) {
            String term = System.getenv("TERM");
            String wt = System.getenv("WT_SESSION");
            return (wt != null) || (term != null && !term.equals("dumb"));
        }
    }

    private String colorize(String text, String ansiCode) {
        if (!ansSupport) return text;
        return ansiCode + text + "\033[0m";
    }

    public void report(MCLException e) {
        Token token = e.getToken();
        int lineNum = token.getLine();
        int colNum = token.getColumn();

        String[] lines = sourceCode.split("\r?\n");
        String errorLine = (lineNum <= lines.length) ? lines[lineNum - 1] : "";
        int underlineLen = Math.max(1, token.getValue() != null ? token.getValue().length() : 1);

        System.err.printf("\033[31;1merror\033[0m: %s%n", e.getMessage());
        System.err.printf(" \033[34;1m-->\033[0m %s:%d:%d%n", fileName, lineNum, colNum);
        System.err.println("   \033[34;1m|\033[0m");
        System.err.printf("%2d \033[34;1m|\033[0m %s%n", lineNum, errorLine);

        String padding = " ".repeat(Math.max(0, colNum - 1));
        String caret = "^".repeat(underlineLen);
        System.err.printf("   \033[34;1m|\033[0m %s\033[31;1m%s\033[0m%n", padding, caret);

        if (e.getHelpHint() != null && !e.getHelpHint().isEmpty()) {
            System.err.println("   \033[34;1m|\033[0m");
            System.err.printf("   \033[36;1m= help:\033[0m %s%n", e.getHelpHint());
        }
        System.err.println();
    }
}
