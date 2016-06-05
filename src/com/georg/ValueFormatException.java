package com.georg;

/**
 * Created by Georg on 04/06/16.
 * <p>
 * Custom Error.
 */
public class ValueFormatException extends Exception {
    public ValueFormatException(String msg) {
        super(msg);
    }
}
