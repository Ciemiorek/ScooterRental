package com.ciemiorek.ScooterSystem.repository;

import com.ciemiorek.ScooterSystem.model.UserAccount;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserAccountRepository extends CrudRepository<UserAccount, Long> {
    List<UserAccount> findByOwnerEmail ( String ownerEmail);

}
