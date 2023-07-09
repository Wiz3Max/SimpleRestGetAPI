package com.wiz3max.simplerest.exception;

public class FileException extends AppException{

    public FileException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public FileException(String message, ErrorCode errorCode, Throwable e) {
        super(message, errorCode, e);
    }
}
