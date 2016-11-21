package com.zz.parser;

public class ClassParseException extends RuntimeException {

    public ClassParseException() {
        super();
    }

    public ClassParseException(String message) {
        super(message);
    }

    public ClassParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClassParseException(Throwable cause) {
        super(cause);
    }
}
