package com.ciemiorek.ScooterSystem.service;

import com.ciemiorek.ScooterSystem.api.request.CreateUserAccountRequest;
import com.ciemiorek.ScooterSystem.api.response.BasicResponse;
import com.ciemiorek.ScooterSystem.api.response.CreateUserAccountResponse;
import com.ciemiorek.ScooterSystem.api.response.ReturnInformationAboutScooterResponse;
import org.springframework.http.ResponseEntity;

public interface UserAccountService {
    ResponseEntity<CreateUserAccountResponse> createUserAccount (CreateUserAccountRequest request);
    ResponseEntity<BasicResponse> rechargeUserAccount (Long accountId, String amount);
    ResponseEntity<ReturnInformationAboutScooterResponse> getInformationAboutRentScooterByEmail(String email);
}
