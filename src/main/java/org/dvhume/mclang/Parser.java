package org.dvhume.mclang;

import org.dvhume.mclang.ast.ASTNode;
import org.dvhume.mclang.ast.ProgramNode;
import org.dvhume.mclang.ast.ProgramNode.SayStatementNode;
import org.dvhume.mclang.ast.ProgramNode.ScoreboardStatementNode;
import org.dvhume.mclang.ast.ProgramNode.ExecuteIfNode;
import org.dvhume.mclang.errors.ErrorReporter;
import org.dvhume.mclang.errors.ParserError;
import org.dvhume.mclang.lexer.Token;
import org.dvhume.mclang.lexer.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    private final List<Token> tokens;
    private int current = 0;
    private final ErrorReporter errorReporter;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.errorReporter = null;
    }

    public Parser(List<Token> tokens, ErrorReporter errorReporter) {
        this.tokens = tokens;
        this.errorReporter = errorReporter;
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
        throw new ParserError(token, "Command expected, received: " + token.getValue(), "Use 'say', 'scoreboard', or 'execute'");
    }

    private SayStatementNode parseSay() {
        Token sayToken = consume(TokenType.SAY, "Expected: 'say'", "'say {arg}'");
        consume(TokenType.LBRACE, "'{'", "You forgot the opening curly brace.");

        List<Token> values = new ArrayList<>();

        // Проверяем, не пустой ли блок
        if (peek().getType() == TokenType.RBRACE) {
            // Пустой say {} - выдаём предупреждение
            if (errorReporter != null) {
                errorReporter.reportWarning(
                        sayToken,
                        "Empty 'say' statement does nothing",
                        "Consider adding some content inside say {}"
                );
            } else {
                System.err.println("warning: Empty 'say' statement");
            }
        } else {
            Token value = parseArgument();
            values.add(value);

            while (!isAtEnd() && peek().getType() != TokenType.RBRACE) {
                if (peek().getType() != TokenType.COMMA) {
                    Token currentToken = peek();
                    throw new ParserError(
                            currentToken,
                            "Expected ',' between arguments or '}' to close the block",
                            "Separate arguments with commas: say {\"str\", arg, arg2}"
                    );
                }

                consume(TokenType.COMMA, "',' between arguments", "Arguments must be separated by commas if there are several");

                // После запятой должен быть аргумент
                if (peek().getType() == TokenType.RBRACE) {
                    throw new ParserError(
                            peek(),
                            "Expected argument after comma",
                            "Remove trailing comma or add an argument"
                    );
                }

                Token nextValue = parseArgument();
                values.add(nextValue);
            }
        }

        // Проверяем, есть ли закрывающая скобка
        if (isAtEnd()) {
            throw new ParserError(
                    tokens.get(tokens.size() - 1),
                    "Expected '}' after 'say' block",
                    "'say' must have both opening and closing curly braces"
            );
        }

        consume(TokenType.RBRACE, "Expected '}' after 'say'", "'say' must have both opening and closing curly braces");

        return new SayStatementNode(values);
    }

    private Token parseArgument() {
        Token value = advance();

        if (value.getType() != TokenType.STRING &&
                value.getType() != TokenType.NUMBER &&
                value.getType() != TokenType.VARIABLE) {

            throw new ParserError(
                    value,
                    "The 'say' command accepts a string, number or variable!",
                    "Use a string, number, or variable inside say {}"
            );
        }

        return value;
    }

    private ScoreboardStatementNode parseScoreboard() {
        consume(TokenType.SCOREBOARD, "Expected 'scoreboard'", "'scoreboard <set/add> <name> <value>");

        Token modeToken = advance();
        if (modeToken.getType() != TokenType.SET && modeToken.getType() != TokenType.ADD) {
            throw new ParserError(modeToken, "Expected 'set' or 'add'", "Use 'set' to assign or 'add' to increment");
        }

        Token varToken = consume(TokenType.IDENTIFIER, "variable name", "Variables must have a name");
        Token valueToken = advance();

        if (valueToken.getType() != TokenType.NUMBER && valueToken.getType() != TokenType.VARIABLE) {
            throw new ParserError(valueToken, "The value must be a number or a variable", "Use a number or an existing variable");
        }
        return new ScoreboardStatementNode(modeToken.getValue(), varToken.getValue(), valueToken);
    }

    private ASTNode parseExecute() {
        consume(TokenType.EXECUTE, "Expected 'execute'", "'execute'");
        consume(TokenType.IF, "'if'", "'execute' requires the presence of 'if' because it is part of it's structure");
        consume(TokenType.SCORE, "'score'", "'score' is required to take the name of your variable");

        Token varToken = consume(TokenType.IDENTIFIER, "variable name", "Enter the name of the variable that will be used in the condition");
        consume(TokenType.MATCHES, "'matches'", "'matches' is needed to establish the condition");

        Token expectedValue = advance();
        consume(TokenType.RUN, "'run'", "run executes the command if the condition is true (for 'if') or false (for 'else')");

        ASTNode thenBranch = parseStatement();
        ASTNode elseBranch = null;
        if (peek().getType() == TokenType.ELSE) {
            consume(TokenType.ELSE, "Else", "<else>");
            consume(TokenType.RUN, "'run'", "run executes the command if the condition is true (for 'if') or false (for 'else')");
            if (isAtEnd()) {
                throw new ParserError(peek(), "The 'else' branch cannot be empty. Expected a command", "Add a command after 'else run'");
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

    private Token consume(TokenType type, String expected, String help) {
        if (peek().getType() == type) return advance();
        throw new ParserError(peek(), "expected '" + expected + "', found '" + peek().getValue() + "'", help);
    }
}