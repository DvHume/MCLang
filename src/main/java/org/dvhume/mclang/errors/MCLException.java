package org.dvhume.mclang.errors;

/**
 * @author DvHume
 */

public class MCLException extends RuntimeException {
    private final int line;
    public MCLException(int line, String msg) {
        super(msg);
        this.line = line;
    }

    public int getLine() { return line; }
}
