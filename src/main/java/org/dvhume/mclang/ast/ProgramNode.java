package org.dvhume.mclang.ast;

import org.dvhume.mclang.lexer.Token;

import java.util.List;

public class ProgramNode extends ASTNode{

    private final List<ASTNode> statements;

    public ProgramNode(List<ASTNode> statements) {
        this.statements = statements;
    }

    public List<ASTNode> getStatements() { return statements; }

    public static class SayStatementNode extends ASTNode {

        private final List<Token> valueTokens; //String, number or variable

        public SayStatementNode(List<Token> valueTokens) {
            this.valueTokens = valueTokens;
        }

        public List<Token> getValueTokens() { return valueTokens; }
    }

    public static class ScoreboardStatementNode extends ASTNode {

        private final String mode;
        private final String varName;
        private final Token valueToken;

        public ScoreboardStatementNode(String mode, String varName, Token valueToken) {
            this.mode = mode;
            this.varName = varName;
            this.valueToken = valueToken;
        }

        public String getMode() { return mode; }
        public String getVarName() { return varName; }
        public Token getValueToken() { return valueToken; }
    }

    public static class ExecuteIfNode extends ASTNode {
        private final String varName;
        private final Token expectedValue; // Number or variable
        private final ASTNode thenBranch; // What? Команда, которая выполнится, если условие верно
        private final ASTNode elseBranch; // Команда, которая выполнится в else

        public ExecuteIfNode(String varName, Token expectedValue, ASTNode thenBranch, ASTNode elseBranch) {
            this.varName = varName;
            this.expectedValue = expectedValue;
            this.thenBranch = thenBranch;
            this.elseBranch = elseBranch;
        }

        public String getVarName() { return varName; }
        public Token getExpectedValue() { return expectedValue; }
        public ASTNode getThenBranch() { return thenBranch; }
        public ASTNode getElseBranch() { return elseBranch; }
    }
}
