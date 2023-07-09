package com.wiz3max.simplerest.exception;

public class InvalidRequestException extends AppException{
    public InvalidRequestException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public InvalidRequestException(String message, ErrorCode errorCode, Throwable e) {
        super(message, errorCode, e);
    }
}
