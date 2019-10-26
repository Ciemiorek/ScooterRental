package com.ciemiorek.ScooterSystem.api.request;

public class AddScooterRequest {

    private String modelName;
    private Integer maxSpeed;
    private Double rentPrice;
    private Long ScooterDockId;


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

    public Double getRentPrice() {
        return rentPrice;
    }

    public void setRentPrice(Double rentPrice) {
        this.rentPrice = rentPrice;
    }

    public Long getScooterDockId() {
        return ScooterDockId;
    }

    public void setScooterDockId(Long scooterDockId) {
        ScooterDockId = scooterDockId;
    }
}
