package org.dvhume.mclang.lexer;

/*
 * Copyright (c) 2026 DvHume
 *
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for license information
 */
public class Token {

    private final TokenType type;
    private final String value;
    private final int line;

    public Token(TokenType type, String value, int line) {
        this.type = type;
        this.value = value;
        this.line = line;
    }

    public TokenType getType() { return type; }

    public String getValue() { return value; }

    public int getLine() { return line; }

    @Override
    public String toString() {
        return String.format("Token{%s, 's', line=%d}", type, value, line);
    }
}
