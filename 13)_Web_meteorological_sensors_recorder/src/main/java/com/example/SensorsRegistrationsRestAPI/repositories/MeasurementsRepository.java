package com.example.SensorsRegistrationsRestAPI.repositories;

import com.example.SensorsRegistrationsRestAPI.models.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MeasurementsRepository extends JpaRepository<Measurement, Integer> {
    Optional<Measurement> findById(int id);
}
