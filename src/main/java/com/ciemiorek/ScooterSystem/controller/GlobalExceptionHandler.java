package com.ciemiorek.ScooterSystem.controller;

import com.ciemiorek.ScooterSystem.api.response.BasicResponse;
import com.ciemiorek.ScooterSystem.exception.CommonBadRequestException;
import com.ciemiorek.ScooterSystem.exception.CommonConflictException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    BasicResponse handleBadRequestException (CommonBadRequestException exception){
        return BasicResponse.ofError(
                exception.getConstErrorMsg().getErrorCode(),
                exception.getConstErrorMsg().getErrorMsg()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    BasicResponse handleBadRequestException (CommonConflictException exception) {
        return BasicResponse.ofError(
                exception.getConstErrorMsg().getErrorCode(),
                exception.getConstErrorMsg().getErrorMsg()
        );
    }
}