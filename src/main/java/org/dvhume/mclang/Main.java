package org.dvhume.mclang;


import org.dvhume.mclang.ast.ProgramNode;
import org.dvhume.mclang.lexer.Lexer;

import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        if (args.length > 0 && (args[0].equals("--version") || args[0].equals("-v"))) {
            System.out.println("MCLang v0.1.0 (Java 21+)");
        }
        if (args.length < 2 || !args[0].equals("run")) {
            return;
        }
        String filePath = args[1].trim().replaceAll("^\"|\"$", "");

        if (!filePath.toLowerCase().endsWith(".mcl")) {
            System.err.println("Error: Файл должен иметь расширение .mcl");
            System.err.println("Вы передали имя файла: '" + filePath + "'");
            return;
        }

        try {
            Path path = Path.of(filePath);
            if (!Files.exists(path)) {
                System.err.println("Error: File '" + filePath + "' not found");
                return;
            }

            String code = Files.readString(path);

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

    private static void printUsage() {
        System.out.println("Use:");
        System.out.println("mclang run <filename.mcl> Run File");
    }
}