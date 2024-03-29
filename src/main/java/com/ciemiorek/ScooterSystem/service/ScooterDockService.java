package com.ciemiorek.ScooterSystem.service;

import com.ciemiorek.ScooterSystem.api.response.BasicResponse;
import com.ciemiorek.ScooterSystem.model.Scooter;
import org.springframework.http.ResponseEntity;

import java.util.Set;

public interface ScooterDockService {

    ResponseEntity<Set<Scooter>> getAllDockScooters(Long scootersDockId);

    ResponseEntity<BasicResponse> removeScooterFromDoc(Long scooterDockId, Long scooterId);
}
