package com.ciemiorek.ScooterSystem.service.impl;

import com.ciemiorek.ScooterSystem.common.MsgSource;
import com.ciemiorek.ScooterSystem.exception.CommonConflictException;
import com.ciemiorek.ScooterSystem.model.Scooter;
import com.ciemiorek.ScooterSystem.model.ScooterDock;
import com.ciemiorek.ScooterSystem.repository.ScooterDockRepository;
import com.ciemiorek.ScooterSystem.service.AbstractCommonService;
import com.ciemiorek.ScooterSystem.service.ScooterDockService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class ScooterDockServiceImpl extends AbstractCommonService implements ScooterDockService {
    private ScooterDockRepository scooterDockRepository;

    public ScooterDockServiceImpl(MsgSource msgSource, ScooterDockRepository scooterDockRepository) {
        super(msgSource);
        this.scooterDockRepository = scooterDockRepository;
    }

    @Override
    public ResponseEntity<Set<Scooter>> getAllDockScooters(Long scootersDockId) {
        Optional<ScooterDock> optionalScooterDock = scooterDockRepository.findById(scootersDockId);
        if (!optionalScooterDock.isPresent()){
            throw new CommonConflictException(msgSource.Err008);

        }
        return ResponseEntity.ok(optionalScooterDock.get().getScooters());
    }
}
