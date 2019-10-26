package com.ciemiorek.ScooterSystem.service;

import com.ciemiorek.ScooterSystem.api.response.BasicResponse;
import org.springframework.http.ResponseEntity;

public interface RentalService {

    ResponseEntity<BasicResponse> rentScooter (Long scooterId, Long accountId);
}
