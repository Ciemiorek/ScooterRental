package com.ciemiorek.ScooterSystem.service.impl;

import com.ciemiorek.ScooterSystem.common.MsgSource;
import com.ciemiorek.ScooterSystem.api.response.BasicResponse;
import com.ciemiorek.ScooterSystem.exception.CommonConflictException;
import com.ciemiorek.ScooterSystem.model.Scooter;
import com.ciemiorek.ScooterSystem.model.UserAccount;
import com.ciemiorek.ScooterSystem.repository.ScooterRepository;
import com.ciemiorek.ScooterSystem.repository.UserAccountRepository;
import com.ciemiorek.ScooterSystem.service.AbstractCommonService;
import com.ciemiorek.ScooterSystem.service.RentalService;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

public class RentalServiceImpl extends AbstractCommonService implements RentalService {
    private UserAccountRepository userAccountRepository;
    private ScooterRepository scooterRepository;

    public RentalServiceImpl(MsgSource msgSource, UserAccountRepository userAccountRepository, ScooterRepository scooterRepository) {
        super(msgSource);
        this.userAccountRepository = userAccountRepository;
        this.scooterRepository = scooterRepository;
    }

    @Override
    public ResponseEntity<BasicResponse> rentScooter(Long scooterId, Long accountId) {
        UserAccount userAccount = extractUserAccountFromRepository(accountId);
        Scooter scooter = extractScooterFromRepository(scooterId);
        checkScooterIsAvailableToRent(scooter);
        checkUserAccountAlreadyHaveRental(userAccount);
        checkAccountHaveEnoughMoney(userAccount, scooter.getRentalPrice());
        finalizeScooterRental(userAccount, scooter);

        return ResponseEntity.ok(BasicResponse.of(msgSource.OK004));

    }

    private Scooter extractScooterFromRepository(Long scooterId) {
        Optional<Scooter> optionalScooter = scooterRepository.findById(scooterId);
        if (!optionalScooter.isPresent()) {
            throw new CommonConflictException(msgSource.Err010);
        }
        return optionalScooter.get();
    }

    private void finalizeScooterRental(UserAccount userAccount, Scooter scooter) {
        userAccount.setBalance(userAccount.getBalance().subtract(scooter.getRentalPrice()));
        scooter.setScooterDock(null);
        scooter.setUserAccount(userAccount);
        scooterRepository.save(scooter);
    }

    private void checkAccountHaveEnoughMoney(UserAccount userAccount, BigDecimal rentalPrice) {
        if (userAccount.getBalance().compareTo(rentalPrice) < 0) {
            throw new CommonConflictException(msgSource.Err013);
        }
    }

    private void checkUserAccountAlreadyHaveRental(UserAccount userAccount) {
        if (userAccount.getScooter() !=null) {
            throw new CommonConflictException(msgSource.Err012);
        }
    }

    private void checkScooterIsAvailableToRent(Scooter scooter) {
        if (scooter.getScooterDock() == null || scooter.getUserAccount() != null) {
            throw new CommonConflictException(msgSource.Err011);
        }
    }

    private UserAccount extractUserAccountFromRepository(Long accountId) {
        Optional<UserAccount> optionalUserAccount = userAccountRepository.findById(accountId);
        if (!optionalUserAccount.isPresent()) {
            throw new CommonConflictException(msgSource.Err006);
        }
        return optionalUserAccount.get();
    }
}
