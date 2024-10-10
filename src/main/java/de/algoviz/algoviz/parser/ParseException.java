package de.algoviz.algoviz.parser;

/**
 * This exception is thrown when a parsing error occurs.
 *
 * @author Tim
 * @version 1.0
 */
public class ParseException extends Exception {

    /**
     * Constructs a ParseException with the specified detail message.
     *
     * @param s the detail message
     */
    public ParseException(String s) {
        super(s);
    }
}
