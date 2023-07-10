package com.wiz3max.simplerest.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    INVALID_FIELD_NAME("2001001", HttpStatus.BAD_REQUEST),
    INVALID_CRITERIA_FIELD_SYNTAX("2001002", HttpStatus.BAD_REQUEST),
    INVALID_CRITERIA_OPERATOR("2001003", HttpStatus.BAD_REQUEST),
    INVALID_SORT_TYPE("2001004", HttpStatus.BAD_REQUEST),
    FILE_CANNOT_OPEN("3001011", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_ILLEGAL_FORMAT("3001021", HttpStatus.INTERNAL_SERVER_ERROR),
    DATA_TYPE_NOT_FOUND("3001022", HttpStatus.NOT_IMPLEMENTED),
    INTERNAL_ERROR("5001001", HttpStatus.INTERNAL_SERVER_ERROR);

    private String code;
    private HttpStatus httpStatus;

    ErrorCode(String code, HttpStatus httpStatus) {
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public String getCode(){
        return this.code;
    }

    public HttpStatus getHttpStatus(){
        return this.httpStatus;
    }
}
