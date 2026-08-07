package org.dvhume.mclang.lexer;

public enum TokenType {
    // COMMANDS
    SAY, SCOREBOARD, EXECUTE, IF, SCORE, MATCHES, RUN, EFFECT,
    SET, ADD,
    // OPERATORS
    PLUS, MINUS, STAR, SLASH,EQUAL,
    // LITERALS
    IDENTIFIER, NUMBER, STRING, VARIABLE,
    EOF
}
