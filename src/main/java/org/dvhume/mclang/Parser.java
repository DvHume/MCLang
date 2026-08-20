package org.dvhume.mclang;

import org.dvhume.mclang.ast.ASTNode;
import org.dvhume.mclang.ast.ProgramNode;
import org.dvhume.mclang.ast.ProgramNode.SayStatementNode;
import org.dvhume.mclang.ast.ProgramNode.ScoreboardStatementNode;
import org.dvhume.mclang.ast.ProgramNode.ExecuteIfNode;
import org.dvhume.mclang.lexer.Token;
import org.dvhume.mclang.lexer.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public ProgramNode parse() {
        List<ASTNode> statements = new ArrayList<>();

        while (!isAtEnd()) {
            statements.add(parseStatement());
        }
        return new ProgramNode(statements);
    }

    private ASTNode parseStatement() {
        Token token = peek();

        if (token.getType() == TokenType.SAY) {
            return parseSay();
        } else if (token.getType() == TokenType.SCOREBOARD) {
            return parseScoreboard();
        } else if (token.getType() == TokenType.EXECUTE) {
            return parseExecute();
        }
        throw new RuntimeException("String " + token.getLine() + ": Command expected, received: " + token.getValue());
    }

    private SayStatementNode parseSay() {
        consume(TokenType.SAY, "Expected: 'say'");
        consume(TokenType.LBRACE, "Expected '{' after 'say'");

        List<Token> values = new ArrayList<>();

        // Первый аргумент
        Token value = advance();

        if (value.getType() != TokenType.STRING &&
                value.getType() != TokenType.NUMBER &&
                value.getType() != TokenType.VARIABLE) {

            throw new RuntimeException(
                    "String: " + value.getLine() +
                            ": The 'say' command accept a string, number or variable!"
            );
        }

        values.add(value);

        // Остальные аргументы
        while (!isAtEnd() && peek().getType() != TokenType.RBRACE) {

            // Если есть ещё аргумент — между ними ОБЯЗАТЕЛЬНА запятая
            consume(TokenType.COMMA, "Expected ',' between arguments");

            Token nextValue = advance();

            if (nextValue.getType() != TokenType.STRING &&
                    nextValue.getType() != TokenType.NUMBER &&
                    nextValue.getType() != TokenType.VARIABLE) {

                throw new RuntimeException(
                        "String: " + nextValue.getLine() +
                                ": The 'say' command accept a string, number or variable!"
                );
            }

            values.add(nextValue);
        }

        if (isAtEnd()) {
            throw new RuntimeException(
                    "String " + peek().getLine() +
                            ": Expected '}' after 'say'"
            );
        }

        consume(TokenType.RBRACE, "Expected '}' after 'say'");

        return new SayStatementNode(values);
    }

    private ScoreboardStatementNode parseScoreboard() {
        consume(TokenType.SCOREBOARD, "Expected 'scoreboard'");

        Token modeToken = advance();
        if (modeToken.getType() != TokenType.SET && modeToken.getType() != TokenType.ADD) {
            throw new RuntimeException("String " + modeToken.getLine() + ": Expected 'set' or 'add'");
        }

        Token varToken = consume(TokenType.IDENTIFIER, "Expected variable name");
        Token valueToken = advance();

        if (valueToken.getType() != TokenType.NUMBER && valueToken.getType() != TokenType.VARIABLE) {
            throw new RuntimeException("String " + valueToken.getLine() + ": The value must be a number or a variable");
        }
        return new ScoreboardStatementNode(modeToken.getValue(), varToken.getValue(), valueToken);
    }

    private ASTNode parseExecute() {
        consume(TokenType.EXECUTE, "Expected 'execute'");
        consume(TokenType.IF, "Expected 'if'");
        consume(TokenType.SCORE, "Expected 'score'");

        Token varToken = consume(TokenType.IDENTIFIER, "Expected variable name");
        consume(TokenType.MATCHES, "Expected 'matches'");

        Token expectedValue = advance();
        consume(TokenType.RUN, "Expected 'run'");

        ASTNode thenBranch = parseStatement();
        ASTNode elseBranch = null;
        if (peek().getType() == TokenType.ELSE) {
            consume(TokenType.ELSE, "Else");
            consume(TokenType.RUN, "\nError: Expected 'run'");
            if (isAtEnd()) {
                throw new RuntimeException("\nString " + peek().getLine() + ": The 'else' branch cannot be empty. Expected a command\n");
            }
            elseBranch = parseStatement();
        }
        return new ExecuteIfNode(varToken.getValue(), expectedValue, thenBranch, elseBranch);
    }

    private boolean isAtEnd() {
        return peek().getType() == TokenType.EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return tokens.get(current - 1);
    }

    private Token consume(TokenType type, String errorMSG) {
        if (peek().getType() == type) return advance();
        throw new RuntimeException("String " + peek().getLine() + ": " + errorMSG);
    }
}
