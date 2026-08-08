package org.dvhume.mclang;


import org.dvhume.mclang.ast.ProgramNode;
import org.dvhume.mclang.lexer.Lexer;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author DvHume
 */

public class Main {
    public static void main(String[] args) {
        if (args.length > 0 && (args[0].equals("--version") || args[0].equals("-v"))) {
            System.out.println("MCLang v0.1.0 (Java 21+)");
            return;
        }
        if (args.length < 2 || !args[0].equals("run")) {
            printUsage();
            return;
        }
        String filePath = args[1].trim().replaceAll("^\"|\"$", "");

        if (!filePath.toLowerCase().endsWith(".mcl")) {
            System.err.println("Error: file must have extension <.mcl>");
            System.err.println("You passed the file name: '" + filePath + "'");
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
            System.err.println("Runtime error");
            e.printStackTrace();
        }
    }

    private static void printUsage() {
        System.out.print("\nUnknown command ");
        System.out.println("Use: mclang run <file.mcl> Run File");
    }
}