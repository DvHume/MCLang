package org.dvhume.mclang.lexer;

import java.util.ArrayList;
import java.util.List;

/*
 * Copyright (c) 2026 DvHume
 *
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for license information
 */
public class Lexer {

    private final String code;
    private  int pos = 0;
    private int line = 1;

    public Lexer(String code) {
        this.code = code;
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();

        while (pos < code.length()) {
            char c = peek();

            if (c == ' ' || c == '\r' || c == '\t') {
                advance();
            } else if (c == '\n') {
                line++;
                advance();
            } else if (c == '#') {
                while (pos < code.length() && peek() != '\n') { advance(); }
            } else if (c == '"') {
                tokens.add(readString());
            } else if (c == '$') {
                tokens.add(readVariable());
            } else if (Character.isDigit(c) || (c == '-' && Character.isDigit(peekNext()))) {
                tokens.add(readNumber());
            } else if (Character.isLetter(c) || c == '_') {
                tokens.add(readIdentifier());
            } else if (c == '+') {
                advance();
                tokens.add(new Token(TokenType.PLUS, "+", line));
            } else if (c == '-') {
                if (Character.isDigit(peekNext())) {
                    tokens.add(readNumber());
                } else {
                    advance();
                    tokens.add(new Token(TokenType.MINUS, "-", line));
                }
            }
            else if (c == '*') { advance(); tokens.add(new Token(TokenType.STAR, "*", line)); }
            else if (c == '/') { advance(); tokens.add(new Token(TokenType.SLASH, "/", line)); }
            else if (c == '=') { advance(); tokens.add(new Token(TokenType.EQUAL, "=", line)); }
            else {
                throw new RuntimeException("String " + line + ": Unknown symbol '" + c + "'");
            }
        }
        tokens.add(new Token(TokenType.EOF, "", line));
        return tokens;
    }

    private char peek() {
        return code.charAt(pos);
    }

    private char peekNext() {
        return (pos + 1 < code.length() ? code.charAt(pos + 1) : '\0');
    }

    private char advance() {
        return code.charAt(pos++);
    }

    private Token readString() {
        int startLine = line;
        advance();

        StringBuilder sb = new StringBuilder();
        while (pos < code.length() && peek() != '"') {
            char c = advance();
            if (c == '\n') {
                line++;
            }
            sb.append(c);
        }
        if (pos >= code.length()) {
            throw new RuntimeException("String " + startLine + ": Unclosed quotation mark!");
        }
        advance();
        return new Token(TokenType.STRING, sb.toString(), startLine);
    }

    private Token readVariable() {
        advance();

        StringBuilder sb = new StringBuilder();
        while (pos < code.length() && (Character.isLetterOrDigit(peek()) || peek() == '_')) {
            sb.append(advance());
        }
        return new Token(TokenType.VARIABLE, sb.toString(), line);
    }

    private Token readNumber() {
        StringBuilder sb = new StringBuilder();
        if (peek() == '-') {
            sb.append(advance());
        }
        while (pos < code.length() && Character.isDigit(peek())) {
            sb.append(advance());
        }
        return new Token(TokenType.NUMBER, sb.toString(), line);
    }

    private Token readIdentifier() {
        StringBuilder sb = new StringBuilder();
        while (pos < code.length() && (Character.isLetterOrDigit(peek()) || peek() == '_')) {
            sb.append(advance());
        }
        String word = sb.toString();

        return switch (word) {
            case "say" -> new Token(TokenType.SAY, word, line);
            case "scoreboard" -> new Token(TokenType.SCOREBOARD, word, line);
            case "set" -> new Token(TokenType.SET, word, line);
            case "add" -> new Token(TokenType.ADD, word, line);
            case "execute" -> new Token(TokenType.EXECUTE, word, line);
            case "if" -> new Token(TokenType.IF, word, line);
            case "score" -> new Token(TokenType.SCORE, word, line);
            case "matches" -> new Token(TokenType.MATCHES, word, line);
            case "run" -> new Token(TokenType.RUN, word, line);
            default -> new Token(TokenType.IDENTIFIER, word, line);
        };
    }
}
