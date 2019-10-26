package com.ciemiorek.ScooterSystem.service.impl;

import com.ciemiorek.ScooterSystem.common.MsgSource;
import com.ciemiorek.ScooterSystem.api.request.AddScooterRequest;
import com.ciemiorek.ScooterSystem.api.response.AddScooterResponse;
import com.ciemiorek.ScooterSystem.exception.CommonBadRequestException;
import com.ciemiorek.ScooterSystem.exception.CommonConflictException;
import com.ciemiorek.ScooterSystem.model.Scooter;
import com.ciemiorek.ScooterSystem.model.ScooterDock;
import com.ciemiorek.ScooterSystem.repository.ScooterDockRepository;
import com.ciemiorek.ScooterSystem.repository.ScooterRepository;
import com.ciemiorek.ScooterSystem.service.AbstractCommonService;
import com.ciemiorek.ScooterSystem.service.ScooterService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

import static com.ciemiorek.ScooterSystem.common.ValidationUtils.isNullOrEmpty;
import static com.ciemiorek.ScooterSystem.common.ValidationUtils.isUncorrectedMaxSpeed;
import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;


@Service
public class ScooterServiceImpl extends AbstractCommonService implements ScooterService {

    private ScooterRepository scooterRepository;
    private ScooterDockRepository scooterDockRepository;

    public ScooterServiceImpl(MsgSource msgSource, ScooterRepository scooterRepository, ScooterDockRepository scooterDockRepository) {
        super(msgSource);
        this.scooterRepository = scooterRepository;
        this.scooterDockRepository = scooterDockRepository;
    }

    @Override
    @Transactional
    public ResponseEntity<AddScooterResponse> addScooter(AddScooterRequest request) {
        validateAddScooterRequest(request);
        ScooterDock scooterDock = extractDockFromRepository(request.getScooterDockId());
        checkIsAvailablePlaceInDock(scooterDock);
        Scooter addedScooter = addScooterToDaraSource(request, scooterDock);
        return ResponseEntity.ok(new AddScooterResponse(msgSource.OK003, addedScooter.getId()));
    }

    private void validateAddScooterRequest(AddScooterRequest request) {
        if (isNullOrEmpty(request.getModelName())
                || isNull(request.getRentPrice())
                || isNull(request.getMaxSpeed())
                || isNull(request.getScooterDockId())) {
            throw new CommonBadRequestException(msgSource.Err001);
        }

        if (isUncorrectedMaxSpeed(request.getMaxSpeed())){
            throw new CommonConflictException(msgSource.Err007);
        }
    }

    private ScooterDock extractDockFromRepository(Long scooterDockId) {
        Optional<ScooterDock> optionalScooterDock = scooterDockRepository.findById(scooterDockId);
        if (!optionalScooterDock.isPresent()){
            throw new CommonConflictException(msgSource.Err008);
        }
        return optionalScooterDock.get();
    }

    private void checkIsAvailablePlaceInDock ( ScooterDock scooterDock) {
        if (scooterDock.getAvailablePlace()<=scooterDock.getScooters().size()){
            throw new CommonConflictException(msgSource.Err009);
        }
    }
    private Scooter addScooterToDaraSource (AddScooterRequest request, ScooterDock scooterDock) {
        Scooter scooter = new Scooter();
        scooter.setModelName(request.getModelName());
        scooter.setMaxSpeed(request.getMaxSpeed());
        scooter.setRentalPrice(new BigDecimal(request.getRentPrice()));
        scooter.setScooterDock(scooterDock);

        return scooterRepository.save(scooter);
    }


}
