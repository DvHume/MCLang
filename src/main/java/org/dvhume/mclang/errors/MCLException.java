package org.dvhume.mclang.errors;

/**
 * @author DvHume
 */

public class MCLException extends Exception {
    private final int line;
    public MCLException(int line, String msg) {
        super(msg);
        this.line = line;
    }

    public int getLine() { return line; }
}
