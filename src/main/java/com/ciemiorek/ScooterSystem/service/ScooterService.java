package com.ciemiorek.ScooterSystem.service;

import com.ciemiorek.ScooterSystem.api.request.AddScooterRequest;
import com.ciemiorek.ScooterSystem.api.response.AddScooterResponse;
import org.springframework.http.ResponseEntity;

public interface ScooterService {
    ResponseEntity<AddScooterResponse> addScooter (AddScooterRequest request);
}
