package org.dvhume.mclang;

import org.dvhume.mclang.ast.ASTNode;
import org.dvhume.mclang.ast.ProgramNode;
import org.dvhume.mclang.lexer.Token;

public class Interpreter {

    private final Environment env = new Environment();

    public void interpret(ProgramNode program) {
        for (ASTNode stmt: program.getStatements()) {
            execute(stmt);
        }
    }

    private void execute(ASTNode node) {
        if (node instanceof ProgramNode.ScoreboardStatementNode scoreboard) {
            executeScoreboard(scoreboard);
        } else if (node instanceof ProgramNode.SayStatementNode say) {
            executeSay(say);
        } else if (node instanceof ProgramNode.ExecuteIfNode execIf) {
            executeIf(execIf);
        }
    }

    private void executeSay(ProgramNode.SayStatementNode node) {
        for (Token token : node.getValueTokens()) {
            Object result = evaluateToken(token);
            System.out.println(result);
        }
        System.out.println();
    }

    private void executeScoreboard(ProgramNode.ScoreboardStatementNode node) {
        Object val = evaluateToken(node.getValueToken());
        if (!(val instanceof Integer intVal)) {
            throw new RuntimeException("The value in 'scoreboard' must be a number");
        }

        String varName = node.getVarName();
        if ("set".equals(node.getMode())) {
            env.set(varName, intVal);
        } else if ("add".equals(node.getMode())) {
            int current = env.has(varName) ? (int) env.get(varName) : 0;
            env.set(varName, current + intVal);
        }
    }

    private void executeIf(ProgramNode.ExecuteIfNode ifNode) {
        Object actualVal = env.get(ifNode.getVarName());
        Object expectedVal = evaluateToken(ifNode.getExpectedValue());

        boolean condition = actualVal != null && actualVal.equals(expectedVal);
        if (condition) {
            execute(ifNode.getThenBranch());
        } else if (ifNode.getElseBranch() != null) {
            execute(ifNode.getElseBranch());
        }
    }

    private Object evaluateToken(Token token) {
        return switch (token.getType()) {
            case STRING -> token.getValue();
            case NUMBER -> Integer.parseInt(token.getValue());
            case VARIABLE -> env.get(token.getValue());
            default -> throw new RuntimeException("Incorrect token: " + token);
        };
    }
}
