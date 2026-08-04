package org.dvhume.mclang;


import org.dvhume.mclang.ast.ProgramNode;
import org.dvhume.mclang.lexer.Lexer;

import java.nio.file.Files;
import java.nio.file.Path;

/*
 * Copyright (c) 2026 DvHume
 * 
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for license information
 */
 public class Main {
    public static void main(String[] args) {
        String fileName = args.length > 0 ? args[0] : "script.mcl";

        try {
            String code = Files.readString(Path.of(fileName));

            Lexer lexer = new Lexer(code);
            var tokens = lexer.tokenize();

            Parser parser = new Parser(tokens);
            ProgramNode programNode = parser.parse();

            Interpreter interpreter = new Interpreter();
            interpreter.interpret(programNode);
        } catch (Exception e) {
            System.err.println("Ошибка выполнения");
            e.printStackTrace();
        }
    }
}