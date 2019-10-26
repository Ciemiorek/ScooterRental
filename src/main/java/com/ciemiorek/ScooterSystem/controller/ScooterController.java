package com.ciemiorek.ScooterSystem.controller;

import com.ciemiorek.ScooterSystem.api.request.AddScooterRequest;
import com.ciemiorek.ScooterSystem.api.response.AddScooterResponse;
import com.ciemiorek.ScooterSystem.service.ScooterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("scooter")
public class ScooterController {

    private ScooterService scooterService;

    public ScooterController(ScooterService scooterService) {
        this.scooterService = scooterService;
    }

    public ScooterController() {
    }

    @PostMapping(value = "/add", produces = "application/json")
    public ResponseEntity<AddScooterResponse> addScooter(
            @RequestBody AddScooterRequest request
    ) {
        return scooterService.addScooter(request);
    }

}
