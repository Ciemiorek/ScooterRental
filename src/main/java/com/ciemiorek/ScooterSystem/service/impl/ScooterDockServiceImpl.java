package com.ciemiorek.ScooterSystem.service.impl;

import com.ciemiorek.ScooterSystem.api.response.BasicResponse;
import com.ciemiorek.ScooterSystem.common.MsgSource;
import com.ciemiorek.ScooterSystem.exception.CommonConflictException;
import com.ciemiorek.ScooterSystem.model.Scooter;
import com.ciemiorek.ScooterSystem.model.ScooterDock;
import com.ciemiorek.ScooterSystem.repository.ScooterDockRepository;
import com.ciemiorek.ScooterSystem.repository.ScooterRepository;
import com.ciemiorek.ScooterSystem.service.AbstractCommonService;
import com.ciemiorek.ScooterSystem.service.ScooterDockService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
public class ScooterDockServiceImpl extends AbstractCommonService implements ScooterDockService {
    private ScooterDockRepository scooterDockRepository;
    private ScooterRepository scooterRepository;

    public ScooterDockServiceImpl(MsgSource msgSource, ScooterDockRepository scooterDockRepository, ScooterRepository scooterRepository) {
        super(msgSource);
        this.scooterDockRepository = scooterDockRepository;
        this.scooterRepository = scooterRepository;
    }

    @Override
    public ResponseEntity<Set<Scooter>> getAllDockScooters(Long scootersDockId) {
        Optional<ScooterDock> optionalScooterDock = scooterDockRepository.findById(scootersDockId);
        if (!optionalScooterDock.isPresent()) {
            throw new CommonConflictException(msgSource.Err008);

        }
        return ResponseEntity.ok(optionalScooterDock.get().getScooters());
    }

    @Override
    @Transactional
    public ResponseEntity<BasicResponse> removeScooterFromDoc(Long scooterDockId, Long scooterId) {
        checkScooterDockExist(scooterDockId);
        checkScooterExist(scooterId);
        checkScootersDock(scooterDockId,scooterId);
        Optional<Scooter> scooterOptional = scooterRepository.findById(scooterId);
        Scooter scooter = scooterOptional.get();
        scooter.setScooterDock(null);
        scooterRepository.save(scooter);

        return ResponseEntity.ok(BasicResponse.of(msgSource.OK010));
    }

    private void checkScootersDock(Long scooterDockId, Long scooterID) {
        Optional<Scooter> scooterOptional = scooterRepository.findById(scooterID);
        Optional<ScooterDock> scooterDockOptional = scooterDockRepository.findById(scooterDockId);
        ScooterDock scooterDockFromRepository = scooterDockOptional.get();
        ScooterDock scooterDockFromScooter;
        try {
            scooterOptional.get().getScooterDock().getDockName();
            scooterDockFromScooter = scooterOptional.get().getScooterDock();
        }catch (NullPointerException ex){
            throw new CommonConflictException(msgSource.Err017);
        }
        if (!(scooterDockFromRepository==scooterDockFromScooter)){
            throw new CommonConflictException(msgSource.Err018);
        }
    }

    private void checkScooterExist(Long scooterId) {
        Optional<Scooter> scooterOptional = scooterRepository.findById(scooterId);
        if (!scooterOptional.isPresent()) {
            throw new CommonConflictException(msgSource.Err010);
        }
    }

    private void checkScooterDockExist(Long scooterDockId) {
        Optional<ScooterDock> scooterDockOptional = scooterDockRepository.findById(scooterDockId);
        if (!scooterDockOptional.isPresent()) {
            throw new CommonConflictException(msgSource.Err008);
        }
    }
}
