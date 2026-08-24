package org.dvhume.mclang.errors;

/*
 * Created by DvHume in 24.08.2026.
 */

import org.dvhume.mclang.lexer.Token;

public class RuntimeError extends MCLException{

    public RuntimeError(Token token, String msg, String helpHint) {
        super(token, msg, helpHint);
    }
}
