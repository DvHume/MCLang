package org.dvhume.mclang.ast;

import org.dvhume.mclang.lexer.Token;

import java.util.List;

/*
 * Copyright (c) 2026 DvHume
 *
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for license information
 */
public class ProgramNode extends ASTNode{

    private final List<ASTNode> statements;

    public ProgramNode(List<ASTNode> statements) {
        this.statements = statements;
    }

    public List<ASTNode> getStatements() { return statements; }

    public static class SayStatementNode extends ASTNode {

        private final Token valueToken; //Строка, число или переменная

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
}
