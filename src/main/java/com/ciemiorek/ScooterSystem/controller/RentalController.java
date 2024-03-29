package com.ciemiorek.ScooterSystem.controller;

import com.ciemiorek.ScooterSystem.api.response.BasicResponse;
import com.ciemiorek.ScooterSystem.service.RentalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("rental")
public class RentalController {

    private RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }



    @PutMapping (value = "/{scooterId}/scooter", produces = "application/json" )
    public ResponseEntity<BasicResponse> rentScooter (
            @PathVariable Long scooterId,
            @RequestParam Long accountId
    ) {
        return rentalService.rentScooter(scooterId,accountId);
    }

    @PutMapping (value = "/{scooterId}/return", produces ="application/json" )
    public ResponseEntity<BasicResponse> returnScooter (
            @PathVariable Long scooterId,
            @RequestParam Long dockId
    ) {
        return rentalService.returnScooter(scooterId,dockId);
    }

}
