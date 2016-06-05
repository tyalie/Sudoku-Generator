package com.georg;

/**
 * Created by Georg on 04/06/16.
 *
 * Custom Error.
 */
public class ValueFormatException extends Exception {
    public ValueFormatException(String msg) {
        super(msg);
    }
}
