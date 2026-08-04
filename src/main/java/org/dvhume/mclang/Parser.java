package org.dvhume.mclang;

import org.dvhume.mclang.ast.ASTNode;
import org.dvhume.mclang.ast.ProgramNode;
import org.dvhume.mclang.lexer.Token;
import org.dvhume.mclang.lexer.TokenType;

import java.util.ArrayList;
import java.util.List;

/*
 * Copyright (c) 2026 DvHume
 *
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for license information
 */
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
  }
  throw new RuntimeException("String " + token.getLine() + ": Command expected, received: " + token.getValue());
 }

 private ProgramNode.SayStatementNode parseSay() {
  consume(TokenType.SAY, "Expected: 'say'");
  Token value = advance();

  if (value.getType() != TokenType.STRING &&
      value.getType() != TokenType.NUMBER &&
      value.getType() != TokenType.VARIABLE) {
   throw new RuntimeException("String: " + value.getLine() + ": The 'say' command accept a string, number or variable!");
  }
  return new ProgramNode.SayStatementNode(value);
 }

 private ProgramNode.ScoreboardStatementNode parseScoreboard() {
  consume(TokenType.SCOREBOARD, "Expected 'scoreboard'");

  Token modeToken = advance();
  if (modeToken.getType() != TokenType.SET && modeToken.getType() != TokenType.ADD) {
   throw  new RuntimeException("String " + modeToken.getLine() + ": Expected 'set' or 'add'");
  }

  Token varToken = consume(TokenType.IDENTIFIER, "Expected variable name");
  Token valueToken = advance();

  if (valueToken.getType() != TokenType.NUMBER && valueToken.getType() != TokenType.VARIABLE) {
   throw new RuntimeException("String " + valueToken.getLine() + ": The value must be a number or a variable");
  }
  return new ProgramNode.ScoreboardStatementNode(modeToken.getValue(), varToken.getValue(), valueToken);
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
