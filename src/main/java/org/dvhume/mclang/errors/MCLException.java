package org.dvhume.mclang.errors;

import org.dvhume.mclang.lexer.Token;

/**
 * @author DvHume
 */

public class MCLException extends RuntimeException {
    private final Token token;
    private final String helpHint;
    public MCLException(Token token, String msg, String helpHint) {
        super(msg);
        this.token = token;
        this.helpHint = helpHint;
    }

    public Token getToken() { return token; }
    public String getHelpHint() { return helpHint; }
}
