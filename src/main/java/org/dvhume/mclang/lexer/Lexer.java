package org.dvhume.mclang.lexer;

import org.dvhume.mclang.errors.LexerError;

import java.util.ArrayList;
import java.util.List;

/**
 @author No Author
 **/
public class Lexer {

    private final String code;
    private  int pos = 0;
    private int line = 1;
    private int column = 1;

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
                column = 1;
                pos++;
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
            } else {
                int startCol = column;
                if (c == '+') {
                    advance();
                    tokens.add(new Token(TokenType.PLUS, "+", line, startCol));
                } else if (c == '-') {
                    if (Character.isDigit(peekNext())) {
                        tokens.add(readNumber());
                    } else {
                        advance();
                        tokens.add(new Token(TokenType.MINUS, "-", line, startCol));
                    }
                } else if (c == '*') {
                    advance();
                    tokens.add(new Token(TokenType.STAR, "*", line, startCol));
                } else if (c == '/') {
                    advance();
                    tokens.add(new Token(TokenType.SLASH, "/", line, startCol));
                } else if (c == '=') {
                    advance();
                    tokens.add(new Token(TokenType.EQUAL, "=", line, startCol));
                } else if (c == '{') {
                    advance();
                    tokens.add(new Token(TokenType.LBRACE, "{", line, startCol));
                } else if (c == '}') {
                    advance();
                    tokens.add(new Token(TokenType.RBRACE, "}", line, startCol));
                } else if (c == ',') {
                    advance();
                    tokens.add(new Token(TokenType.COMMA, ",", line, startCol));
                } else {
                    throw new LexerError(line, startCol, "Unknown symbol '" + c + "'");
                }
            }
        }
        tokens.add(new Token(TokenType.EOF, "", line, column));
        return tokens;
    }

    private char peek() {
        return code.charAt(pos);
    }

    private char peekNext() {
        return (pos + 1 < code.length() ? code.charAt(pos + 1) : '\0');
    }

    private char advance() {
        char c = code.charAt(pos++);
        column++;
        return c;
    }

    private Token readString() {
        int startLine = line;
        int startCol = column;
        advance();

        StringBuilder sb = new StringBuilder();
        while (pos < code.length() && peek() != '"') {
            char c = peek();
            if (c == '\n') {
                line++;
                column = 1;
                pos++;
            }
            sb.append(advance());
        }
        if (pos >= code.length()) {
            throw new LexerError(startLine, startCol, ": Unterminated string literal");
        }
        advance();
        return new Token(TokenType.STRING, sb.toString(), startLine, startCol);
    }

    private Token readVariable() {
        int startCol = column;
        advance();

        StringBuilder sb = new StringBuilder();
        while (pos < code.length() && (Character.isLetterOrDigit(peek()) || peek() == '_')) {
            sb.append(advance());
        }
        return new Token(TokenType.VARIABLE, sb.toString(), line, startCol);
    }

    private Token readNumber() {
        int startCol = column;
        StringBuilder sb = new StringBuilder();
        if (peek() == '-') {
            sb.append(advance());
        }
        while (pos < code.length() && Character.isDigit(peek())) {
            sb.append(advance());
        }
        return new Token(TokenType.NUMBER, sb.toString(), line, startCol);
    }

    private Token readIdentifier() {
        int startCol = column;
        StringBuilder sb = new StringBuilder();
        while (pos < code.length() && (Character.isLetterOrDigit(peek()) || peek() == '_')) {
            sb.append(advance());
        }
        String word = sb.toString();

        TokenType type = switch (word) {
            case "say" -> TokenType.SAY;
            case "scoreboard" -> TokenType.SCOREBOARD;
            case "set" -> TokenType.SET;
            case "add" -> TokenType.ADD;
            case "execute" -> TokenType.EXECUTE;
            case "if" -> TokenType.IF;
            case "score" -> TokenType.SCORE;
            case "matches" -> TokenType.MATCHES;
            case "run" -> TokenType.RUN;
            case "else" -> TokenType.ELSE;
            default -> TokenType.IDENTIFIER;
        };
        return new Token(type, word, line, startCol);
    }
}
