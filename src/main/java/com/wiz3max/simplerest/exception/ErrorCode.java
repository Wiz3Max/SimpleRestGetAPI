package com.wiz3max.simplerest.exception;

public enum ErrorCode {
    INVALID_FIELD_NAME("2001001"),
    INVALID_CRITERIA_FIELD_SYNTAX("2001002"),
    INVALID_CRITERIA_OPERATOR("2001003"),
    INVALID_SORT_TYPE("2001004"),
    FILE_CANNOT_OPEN("3001011"),
    FILE_ILLEGAL_FORMAT("3001021"),
    DATA_TYPE_NOT_FOUND("3001022"),
    INTERNAL_ERROR("5001001");

    private String code;

    ErrorCode(String code) {
        this.code = code;
    }
}
