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

        private final Token valueToken; //String, number or variable

        public SayStatementNode(Token valueToken) {
            this.valueToken = valueToken;
        }

        public Token getValueToken() { return valueToken; }
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

        public ExecuteIfNode(String varName, Token expectedValue, ASTNode thenBranch) {
            this.varName = varName;
            this.expectedValue = expectedValue;
            this.thenBranch = thenBranch;
        }

        public String getVarName() { return varName; }
        public Token getExpectedValue() { return expectedValue; }
        public ASTNode getThenBranch() { return thenBranch; }
    }
}
