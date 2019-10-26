package com.ciemiorek.ScooterSystem.service;

import com.ciemiorek.ScooterSystem.common.MsgSource;

public abstract class AbstractCommonService {

    protected MsgSource msgSource;

    public AbstractCommonService(MsgSource msgSource) {
        this.msgSource = msgSource;
    }
}
