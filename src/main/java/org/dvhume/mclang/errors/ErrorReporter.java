package org.dvhume.mclang.errors;

/*
 * Created by DvHume in 24.08.2026.
 */

import org.dvhume.mclang.lexer.Token;

public class ErrorReporter {

    private final String sourceCode;
    private final String fileName;
    private final boolean ansiSupport;

    public ErrorReporter(String sourceCode, String fileName) {
        this.sourceCode = sourceCode;
        this.fileName = fileName;
        this.ansiSupport = checkAnsiSupport();
    }

    private boolean checkAnsiSupport() {
        String os = System.getProperty("os.name").toLowerCase();
        if (!os.contains("win")) {
            return true;
        }

        try {
            new ProcessBuilder("cmd", "/c", "echo " + Ansi.RESET).inheritIO().start().waitFor();
            return true;
        } catch (Exception e) {
            String term = System.getenv("TERM");
            String wt = System.getenv("WT_SESSION");
            return (wt != null) || (term != null && !term.equals("dumb"));
        }
    }

    public void report(MCLException e) {
        Token token = e.getToken();
        int lineNum = token.getLine();
        int colNum = token.getColumn();

        String[] lines = sourceCode.split("\r?\n");
        String errorLine = (lineNum <= lines.length) ? lines[lineNum - 1] : "";
        int underlineLen = Math.max(1, token.getValue() != null ? token.getValue().length() : 1);

        String errorTag = Ansi.redBold("error", ansiSupport);
        String arrow = Ansi.blueBold("-->", ansiSupport);
        String pipe = Ansi.blueBold("|", ansiSupport);
        String helpPrefix = Ansi.cyanBold("= help:", ansiSupport);

        System.err.printf("%s: %s%n", errorTag, e.getMessage());
        System.err.printf(" %s %s:%d:%d%n", arrow, fileName, lineNum, colNum);
        System.err.printf("   %s%n", pipe);
        System.err.printf("%2d %s %s%n", lineNum, pipe, errorLine);

        String padding = " ".repeat(Math.max(0, colNum - 1));
        String caret = Ansi.redBold("^".repeat(underlineLen), ansiSupport);
        System.err.printf("   %s %s%s%n", pipe, padding, caret);

        if (e.getHelpHint() != null && !e.getHelpHint().isEmpty()) {
            System.err.printf("   %s%n", pipe);
            System.err.printf("   %s %s%n", helpPrefix, e.getHelpHint());
        }
        System.err.println();
    }
}