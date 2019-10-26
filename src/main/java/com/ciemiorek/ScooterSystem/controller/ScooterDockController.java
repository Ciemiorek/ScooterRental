package com.ciemiorek.ScooterSystem.controller;

import com.ciemiorek.ScooterSystem.model.Scooter;
import com.ciemiorek.ScooterSystem.service.ScooterDockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("scooter-dock")
public class ScooterDockController {
    private ScooterDockService scooterDockService;

    public ScooterDockController() {
    }

    public ScooterDockController(ScooterDockService scooterDockService) {
        this.scooterDockService = scooterDockService;
    }

    @GetMapping(value = "/{scooterDockId}/scooters", produces = "application/json")
    public ResponseEntity<Set<Scooter>> getAllDockScooters(
            @PathVariable Long scooterDockId
    ) {
        return scooterDockService.getAllDockScooters(scooterDockId);
    }
}
