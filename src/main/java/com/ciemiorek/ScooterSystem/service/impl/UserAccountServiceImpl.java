package com.ciemiorek.ScooterSystem.service.impl;

import com.ciemiorek.ScooterSystem.api.response.BalanceResponse;
import com.ciemiorek.ScooterSystem.api.response.ReturnInformationAboutScooterResponse;
import com.ciemiorek.ScooterSystem.common.MsgSource;
import com.ciemiorek.ScooterSystem.api.request.CreateUserAccountRequest;
import com.ciemiorek.ScooterSystem.api.response.BasicResponse;
import com.ciemiorek.ScooterSystem.api.response.CreateUserAccountResponse;
import com.ciemiorek.ScooterSystem.exception.CommonBadRequestException;
import com.ciemiorek.ScooterSystem.exception.CommonConflictException;
import com.ciemiorek.ScooterSystem.model.Scooter;
import com.ciemiorek.ScooterSystem.model.UserAccount;
import com.ciemiorek.ScooterSystem.repository.UserAccountRepository;
import com.ciemiorek.ScooterSystem.service.AbstractCommonService;
import com.ciemiorek.ScooterSystem.service.UserAccountService;
import org.apache.catalina.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.ciemiorek.ScooterSystem.common.ValidationUtils.*;
import static java.util.Objects.isNull;

@Service
public class UserAccountServiceImpl extends AbstractCommonService implements UserAccountService {
    private UserAccountRepository userAccountRepository;

    public UserAccountServiceImpl(MsgSource msgSource, UserAccountRepository userAccountRepository) {
        super(msgSource);
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public ResponseEntity<CreateUserAccountResponse> createUserAccount(CreateUserAccountRequest request) {
        validateCreateAccountRequest(request);
        checkOwnerEmailAlreadyExist(request.getOwnerEmail());
        UserAccount addedAccount = addUserAccountToDataSource(request);
        return ResponseEntity.ok(new CreateUserAccountResponse(msgSource.OK001, addedAccount.getId()));
    }

    @Override
    public ResponseEntity<BasicResponse> rechargeUserAccount(Long accountId, String amount) {
        BigDecimal rechargeAmount = extractAmountToBigDecimal(amount);
        addRechargeAmountToUserAccountBalace(accountId, rechargeAmount);
        return ResponseEntity.ok(BasicResponse.of(msgSource.OK002));
    }

    @Override
    public ResponseEntity<ReturnInformationAboutScooterResponse> getInformationAboutRentScooterByEmail(String email) {
        checkUserExistByEmail(email);
        checkUserHaveRentScooter(email);
        List<UserAccount> listOfUser = userAccountRepository.findByOwnerEmail(email);
        Scooter scooter = listOfUser.get(0).getScooter();
        return ResponseEntity.ok(new ReturnInformationAboutScooterResponse(msgSource.OK005, scooter.getId(), scooter.getModelName(), scooter.getMaxSpeed(), scooter.getRentalPrice()));
    }

    @Override
    public ResponseEntity<BasicResponse> getBalancea(Long userID) {
        checkUserWithIdExist(userID);
        Optional<UserAccount> optionalUserAccount =userAccountRepository.findById(userID);
        UserAccount userAccount = optionalUserAccount.get();
        BigDecimal balance = userAccount.getBalance();
        return ResponseEntity.ok(new BalanceResponse(msgSource.OK007, balance));
    }

    @Override
    @Transactional
    public ResponseEntity<BasicResponse> deleteUser(String email) {
        checkUserExistByEmail(email);
        List<UserAccount> userAccountList = userAccountRepository.findByOwnerEmail(email);
        UserAccount userAccountToRemove =userAccountList.get(0);
        userAccountRepository.delete(userAccountToRemove);
        return ResponseEntity.ok(BasicResponse.of(msgSource.OK008));
    }

    @Override
    @Transactional
    public ResponseEntity<BasicResponse> changeEmail(String accountEmail, String emailToReplace) {
        checkUserExistByEmail(accountEmail);
        List<UserAccount> userAccountsList = userAccountRepository.findByOwnerEmail(accountEmail);
        UserAccount userAccount = userAccountsList.get(0);
        if(isUncorrectedEmail(emailToReplace)){
            throw new CommonConflictException(msgSource.Err002);
        }
        userAccount.setOwnerEmail(emailToReplace);
        userAccountRepository.save(userAccount);
        return ResponseEntity.ok(BasicResponse.of(msgSource.OK009));
    }



    private void addRechargeAmountToUserAccountBalace(Long accountId, BigDecimal rechargeAmount) {
        Optional<UserAccount> userAccountData = userAccountRepository.findById(accountId);
        if (!userAccountData.isPresent()) {
            throw new CommonConflictException(msgSource.Err006);
        }
        UserAccount accountData = userAccountData.get();
        accountData.setBalance(accountData.getBalance().add(rechargeAmount));
        userAccountRepository.save(accountData);
    }

    private BigDecimal extractAmountToBigDecimal(String amount) {
        try {
            return new BigDecimal(amount);
        } catch (NumberFormatException nfe) {
            throw new CommonBadRequestException(msgSource.Err005);
        }

    }

    private void validateCreateAccountRequest(CreateUserAccountRequest request) {
        if (isNullOrEmpty(request.getOwnerUsername())
                || isNullOrEmpty(request.getOwnerEmail())
                || isNull(request.getOwnerAge())) {
            throw new CommonBadRequestException(msgSource.Err001);
        }
        if (isUncorrectedEmail(request.getOwnerEmail())) {
            throw new CommonBadRequestException(msgSource.Err002);

        }
        if (isUncorrectedAge(request.getOwnerAge())) {
            throw new CommonConflictException(msgSource.Err003);
        }
    }

    private void checkOwnerEmailAlreadyExist(String ownerEmail) {
        List<UserAccount> userAccounts = userAccountRepository.findByOwnerEmail(ownerEmail);
        if (!userAccounts.isEmpty()) {
            throw new CommonConflictException(msgSource.Err004);
        }
    }

    private void checkUserWithIdExist(Long userID) {
        Optional<UserAccount> optionalUserAccount = userAccountRepository.findById(userID);
        if (!optionalUserAccount.isPresent()){
            throw new CommonConflictException(msgSource.Err006);
        }

    }


    private UserAccount addUserAccountToDataSource(CreateUserAccountRequest request) {
        UserAccount userAccount = new UserAccount(
                null,
                request.getOwnerEmail(),
                request.getOwnerUsername(),
                request.getOwnerAge(),
                new BigDecimal(0.0),
                LocalDateTime.now()
        );
        return userAccountRepository.save(userAccount);
    }

    private void checkUserHaveRentScooter(String ownerEmail) {
        UserAccount userAccount = userAccountRepository.findByOwnerEmail(ownerEmail).get(0);
        try {
            userAccount.getScooter().getModelName();
        } catch (NullPointerException ex) {
            throw new CommonConflictException(msgSource.Err015);
        }
    }

    private void checkUserExistByEmail(String ownerEmail) {
        List<UserAccount> userAccounts = userAccountRepository.findByOwnerEmail(ownerEmail);
        try {
            userAccounts.get(0);
        } catch (IndexOutOfBoundsException ex) {
            throw new CommonConflictException(msgSource.Err014);
        }

    }
}
