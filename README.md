# MCLang

## What is it?

__MCLang__ is an interpreted programming language that uses __Minecraft__ commands as keywords.

## Is it possible to write a simple program in it?

Uhmm... __NO__

# CLI

```bash
# Lang version
mclang --version
mclang -v

# Run File
mclang run <file.mcl>
```

# How to install?

> I'm assuming you already have Java 21 or higher installed.

Just go to releases, download the archive, unzip it and add the path to the new folder to your PATH

LICENSED by [GPL-3.0](LICENSE)

> [!NOTE]
>
> This is a study project, feel free to scold me for any reason, I wil not read it. Thanks!

## Architecture

```
The project is divided into several main parts:

- ast/ — Abstract Syntax Tree (AST) nodes:
- ASTNode — The base class of AST nodes
- ProgramNode — The root program node and nodes for individual constructs.

- lexer/ — Lexical analysis:
- Lexer — Translates source code into sequences of tokens.

- Token — Represents a single token.

- TokenType — Types of language tokens.

- Environment — Stores program state and variables during execution.

- Interpreter — Implements the AST and implements language constructs.

The remaining classes are located in the root package, as they do not currently require a separate structural unit.
```