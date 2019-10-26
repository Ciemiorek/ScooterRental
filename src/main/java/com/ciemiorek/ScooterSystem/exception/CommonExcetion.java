package com.ciemiorek.ScooterSystem.exception;

import com.ciemiorek.ScooterSystem.common.ConstErrorMsg;

public class CommonExcetion extends RuntimeException {
    private ConstErrorMsg constErrorMsg;

    public CommonExcetion(ConstErrorMsg constErrorMsg) {
        this.constErrorMsg = constErrorMsg;
    }

    public ConstErrorMsg getConstErrorMsg() {
        return constErrorMsg;
    }
}
