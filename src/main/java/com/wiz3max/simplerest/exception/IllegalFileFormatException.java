package com.wiz3max.simplerest.exception;

public class IllegalFileFormatException extends FileException {

    public IllegalFileFormatException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public IllegalFileFormatException(String message, ErrorCode errorCode, Throwable e) {
        super(message, errorCode, e);
    }
}
