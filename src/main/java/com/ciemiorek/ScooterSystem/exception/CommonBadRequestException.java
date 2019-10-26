package com.ciemiorek.ScooterSystem.exception;

import com.ciemiorek.ScooterSystem.common.ConstErrorMsg;

public class CommonBadRequestException extends CommonExcetion {

    public CommonBadRequestException(ConstErrorMsg constErrorMsg) {
        super(constErrorMsg);
    }
}
