package com.wiz3max.simplerest.exception;

public class InternalErrorException extends AppException{
    public InternalErrorException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public InternalErrorException(String message, ErrorCode errorCode, Throwable e) {
        super(message, errorCode, e);
    }
}
