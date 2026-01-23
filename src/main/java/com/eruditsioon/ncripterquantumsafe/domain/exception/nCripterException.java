package com.eruditsioon.ncripterquantumsafe.domain.exception;

public class nCripterException extends RuntimeException {

    public nCripterException(String message) {
        super(message);
    }

    public nCripterException(String message, Throwable cause) {
        super(message, cause);
    }

    public nCripterException(Throwable cause) {
        super(cause);
    }
}
