package com.ciemiorek.ScooterSystem.service.impl;

import com.ciemiorek.ScooterSystem.common.MsgSource;
import com.ciemiorek.ScooterSystem.api.response.BasicResponse;
import com.ciemiorek.ScooterSystem.exception.CommonConflictException;
import com.ciemiorek.ScooterSystem.model.Scooter;
import com.ciemiorek.ScooterSystem.model.ScooterDock;
import com.ciemiorek.ScooterSystem.model.UserAccount;
import com.ciemiorek.ScooterSystem.repository.ScooterDockRepository;
import com.ciemiorek.ScooterSystem.repository.ScooterRepository;
import com.ciemiorek.ScooterSystem.repository.UserAccountRepository;
import com.ciemiorek.ScooterSystem.service.AbstractCommonService;
import com.ciemiorek.ScooterSystem.service.RentalService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
@Service
public class RentalServiceImpl extends AbstractCommonService implements RentalService {
    private UserAccountRepository userAccountRepository;
    private ScooterRepository scooterRepository;
    private ScooterDockRepository scooterDockRepository;

    public RentalServiceImpl(MsgSource msgSource, UserAccountRepository userAccountRepository, ScooterRepository scooterRepository, ScooterDockRepository scooterDockRepository) {
        super(msgSource);
        this.userAccountRepository = userAccountRepository;
        this.scooterRepository = scooterRepository;
        this.scooterDockRepository = scooterDockRepository;
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

    @Override
    @Transactional
    public ResponseEntity<BasicResponse> returnScooter( Long scooterId, Long scooterDockId) {
        System.out.println("1");
        Scooter scooter = extractScooterFromRepository(scooterId);
        System.out.println("2");
        ScooterDock scooterDock = extractScooterDockFromRepository(scooterDockId);
        System.out.println("3");
        UserAccount userAccount = getUserWitchRentScooter(scooter);
        System.out.println(4);
        checkFreePlaceInDock(scooterDock);
        System.out.println("5");
        finalizeScooterReturn(userAccount,scooter,scooterDock);

        return ResponseEntity.ok(BasicResponse.of(msgSource.OK006));
    }

    private void finalizeScooterReturn(UserAccount userAccount, Scooter scooter, ScooterDock scooterDock) {
        userAccount.setScooter(null);
        scooter.setUserAccount(null);
        scooter.setScooterDock(scooterDock);
        scooterDock.getScooters().add(scooter);
        userAccountRepository.save(userAccount);
        scooterDockRepository.save(scooterDock);
    }

    private void checkFreePlaceInDock(ScooterDock scooterDock) {
        if (scooterDock.getAvailablePlace()<=scooterDock.getScooters().size()){
            throw new CommonConflictException(msgSource.Err009);
        }
    }

    private UserAccount getUserWitchRentScooter(Scooter scooter) {
        UserAccount userAccount;
        try {
            userAccount = scooter.getUserAccount();
        }catch (NullPointerException ex){
            throw new CommonConflictException(msgSource.Err016);
        }
        return userAccount;
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

    private ScooterDock extractScooterDockFromRepository(Long scooterDockID){
        Optional<ScooterDock> optionalScooterDock = scooterDockRepository.findById(scooterDockID);
        if (!optionalScooterDock.isPresent()){
            throw new CommonConflictException(msgSource.Err008);
        }
        return optionalScooterDock.get();
    }
}
