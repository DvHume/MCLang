package org.dvhume.mclang.errors;

/*
 * Created by DvHume in 24.08.2026.
 */

import org.dvhume.mclang.lexer.Token;
import org.dvhume.mclang.lexer.TokenType;

public class LexerError extends MCLException{

    public LexerError(int line, int column, String msg) {
        super(new Token(TokenType.ERROR, "", line, column), msg, null);
    }

    public LexerError(int line, int column, String msg, String helpHint) {
        super(new Token(TokenType.ERROR, "", line, column), msg, helpHint);
    }
}
