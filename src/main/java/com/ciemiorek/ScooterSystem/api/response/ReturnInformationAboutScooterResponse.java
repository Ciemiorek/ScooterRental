package com.ciemiorek.ScooterSystem.api.response;

import java.math.BigDecimal;

public class ReturnInformationAboutScooterResponse extends BasicResponse {

    private Long id;
    private String modelName;
    private Integer maxSpeed;
    private BigDecimal rentalPrice;

    public ReturnInformationAboutScooterResponse(String responseMsg, Long id, String modelName, Integer maxSpeed, BigDecimal rentalPrice) {
        super(responseMsg);
        this.id = id;
        this.modelName = modelName;
        this.maxSpeed = maxSpeed;
        this.rentalPrice = rentalPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Integer getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(Integer maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public BigDecimal getRentalPrice() {
        return rentalPrice;
    }

    public void setRentalPrice(BigDecimal rentalPrice) {
        this.rentalPrice = rentalPrice;
    }
}
