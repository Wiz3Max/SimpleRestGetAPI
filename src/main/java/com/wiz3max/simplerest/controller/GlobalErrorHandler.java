package com.wiz3max.simplerest.controller;

import com.wiz3max.simplerest.controller.model.response.BaseResponse;
import com.wiz3max.simplerest.exception.AppException;
import com.wiz3max.simplerest.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@CommonsLog
@ControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<BaseResponse> generalAppExceptionHandler(HttpServletRequest request, Exception ex) {
        log.error(request.getQueryString() == null ? request.getRequestURL() : request.getRequestURL().append("?").append(request.getQueryString()));
        log.error(ex.getMessage(), ex);

        AppException appException = (AppException) ex;

        BaseResponse errorResponse = new BaseResponse();
        errorResponse.setErrorCode(appException.getErrorCode().getCode());
        errorResponse.setErrorMsg(appException.getMessage());
        errorResponse.setNumberRecord(0);

        return new ResponseEntity<>(errorResponse, appException.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<BaseResponse> unexpectedExceptionHandler(HttpServletRequest request, Throwable ex) {
        log.error(request.getQueryString() == null ? request.getRequestURL() : request.getRequestURL().append("?").append(request.getQueryString()));
        log.error(ex.getMessage(), ex);

        BaseResponse errorResponse = new BaseResponse();
        errorResponse.setErrorCode(ErrorCode.INTERNAL_ERROR.getCode());
        errorResponse.setErrorMsg("Found some expected error");
        errorResponse.setNumberRecord(0);

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
