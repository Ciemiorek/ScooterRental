package com.ciemiorek.ScooterSystem.repository;

import com.ciemiorek.ScooterSystem.model.ScooterDock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScooterDockRepository extends JpaRepository<ScooterDock, Long> {
}
