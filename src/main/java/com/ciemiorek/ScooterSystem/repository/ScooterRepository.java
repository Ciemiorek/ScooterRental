package com.ciemiorek.ScooterSystem.repository;

import com.ciemiorek.ScooterSystem.model.Scooter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScooterRepository extends JpaRepository<Scooter, Long> {
}
