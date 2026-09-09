package org.dvhume.mclang.errors;

/*
 * Created by DvHume in 24.08.2026.
 */

import org.dvhume.mclang.lexer.Token;

public class ErrorReporter {

    private final String sourceCode;
    private final String fileName;

    public ErrorReporter(String sourceCode, String fileName) {
        this.sourceCode = sourceCode;
        this.fileName = fileName;
    }

    public void report(MCLException e) {
        Token token = e.getToken();
        int lineNum = token.getLine();
        int colNum = token.getColumn();

        String[] lines = sourceCode.split("\r?\n");
        String errorLine = (lineNum <= lines.length) ? lines[lineNum - 1] : "";
        int underlineLen = Math.max(1, token.getValue() != null ? token.getValue().length() : 1);

        String errorTag = Ansi.redBold("error");
        String arrow = Ansi.blueBold("-->");
        String pipe = Ansi.blueBold("|");
        String helpPrefix = Ansi.cyanBold("= help:");

        System.err.printf("%s: %s%n", errorTag, e.getMessage());
        System.err.printf(" %s %s:%d:%d%n", arrow, fileName, lineNum, colNum);
        System.err.printf("   %s%n", pipe);
        System.err.printf("%2d %s %s%n", lineNum, pipe, errorLine);

        String padding = " ".repeat(Math.max(0, colNum - 1));
        String caret = Ansi.redBold("^".repeat(underlineLen));
        System.err.printf("   %s %s%s%n", pipe, padding, caret);

        if (e.getHelpHint() != null && !e.getHelpHint().isEmpty()) {
            System.err.printf("   %s%n", pipe);
            System.err.printf("   %s %s%n", helpPrefix, e.getHelpHint());
        }
        System.err.println();
    }

    public void reportWarning(Token token, String message, String helpHint) {
        int lineNum = token.getLine();
        int colNum = token.getColumn();

        String[] lines = sourceCode.split("\r?\n");
        String errorLine = (lineNum <= lines.length) ? lines[lineNum - 1] : "";
        int underlineLen = Math.max(1, token.getValue() != null ? token.getValue().length() : 1);

        String warningTag = Ansi.yellowBold("warning");
        String arrow = Ansi.blueBold("-->");
        String pipe = Ansi.blueBold("|");
        String helpPrefix = Ansi.cyanBold("= help:");

        System.err.printf("%s: %s%n", warningTag, message);
        System.err.printf(" %s %s:%d:%d%n", arrow, fileName, lineNum, colNum);
        System.err.printf("   %s%n", pipe);
        System.err.printf("%2d %s %s%n", lineNum, pipe, errorLine);

        String padding = " ".repeat(Math.max(0, colNum - 1));
        String caret = Ansi.yellowBold("^".repeat(Math.max(1, underlineLen)));
        System.err.printf("   %s %s%s%n", pipe, padding, caret);

        if (helpHint != null && !helpHint.isEmpty()) {
            System.err.printf("   %s%n", pipe);
            System.err.printf("   %s %s%n", helpPrefix, helpHint);
        }
        System.err.println();
    }
}