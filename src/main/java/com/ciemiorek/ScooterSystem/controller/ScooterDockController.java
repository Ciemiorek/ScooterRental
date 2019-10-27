package com.ciemiorek.ScooterSystem.controller;

import com.ciemiorek.ScooterSystem.api.response.BasicResponse;
import com.ciemiorek.ScooterSystem.model.Scooter;
import com.ciemiorek.ScooterSystem.service.ScooterDockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("scooter-dock")
public class ScooterDockController {
    private ScooterDockService scooterDockService;


    public ScooterDockController(ScooterDockService scooterDockService) {
        this.scooterDockService = scooterDockService;
    }

    @GetMapping(value = "/{scooterDockId}/scooters", produces = "application/json")
    public ResponseEntity<Set<Scooter>> getAllDockScooters(
            @PathVariable Long scooterDockId
    ) {
        return scooterDockService.getAllDockScooters(scooterDockId);
    }

    @PutMapping(value = "/{scooterDockId}/removeScooter", produces = "application/json")
    public ResponseEntity<BasicResponse> removeScooterFromDock(
            @PathVariable Long scooterDockId,
            @RequestParam Long scooterId
    ) {
        return scooterDockService.removeScooterFromDoc(scooterDockId,scooterId);
    }
}
