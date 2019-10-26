package com.ciemiorek.ScooterSystem.exception;

import com.ciemiorek.ScooterSystem.common.ConstErrorMsg;

public class CommonConflictException extends CommonExcetion {
    public CommonConflictException(ConstErrorMsg constErrorMsg) {
        super(constErrorMsg);
    }
}
