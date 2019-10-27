package com.ciemiorek.ScooterSystem.controller;

import com.ciemiorek.ScooterSystem.api.request.CreateUserAccountRequest;
import com.ciemiorek.ScooterSystem.api.response.BasicResponse;
import com.ciemiorek.ScooterSystem.api.response.CreateUserAccountResponse;
import com.ciemiorek.ScooterSystem.api.response.ReturnInformationAboutScooterResponse;
import com.ciemiorek.ScooterSystem.service.UserAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user-account")
public class UserAccountController {

    private UserAccountService userAccountService;


    public UserAccountController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @PostMapping(value = "/create", produces = "application/json")
    public ResponseEntity<CreateUserAccountResponse> createUserAccount (
            @RequestBody CreateUserAccountRequest request
    ) {
        return userAccountService.createUserAccount(request);
    }

    @PutMapping(value = "/{accountId}/recharge" , produces = "application/json")
    public ResponseEntity<BasicResponse> rechargeUserAccount(
            @PathVariable Long accountId,
            @RequestParam String amount
    ) {
        return userAccountService.rechargeUserAccount(accountId,amount);
    }


    @GetMapping(value = "/scooter", produces = "application/json")
    public ResponseEntity<ReturnInformationAboutScooterResponse> getUserScooter (
            @RequestParam String email
    ) {
        return userAccountService.getInformationAboutRentScooterByEmail(email);
    }

    @GetMapping(value = "/balance", produces = "application/json")
    public ResponseEntity<BasicResponse> getBalance (
            @RequestParam Long userID
    ) {
        return userAccountService.getBalancea(userID);
    }

    @DeleteMapping(value = "/delete", produces = "application/json")
    public ResponseEntity<BasicResponse> deleteUser (
            @RequestParam String email
    ) {
        return userAccountService.deleteUser(email);
    }

    @PutMapping(value = "/{accountEmail}/email" , produces = "application/json")
    public ResponseEntity<BasicResponse> changeEmail(
            @PathVariable String accountEmail,
            @RequestParam String emailToReplace
    ) {
        return userAccountService.changeEmail(accountEmail,emailToReplace);
    }

}
