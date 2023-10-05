package com.example.SensorsRegistrationsRestAPI.services;

import com.example.SensorsRegistrationsRestAPI.models.Sensor;
import com.example.SensorsRegistrationsRestAPI.repositories.SensorsRepository;
import com.example.SensorsRegistrationsRestAPI.utilSensors.SensorNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SensorsService {
    private final SensorsRepository sensorsRepository;

    @Autowired
    public SensorsService(SensorsRepository sensorsRepository) {
        this.sensorsRepository = sensorsRepository;
    }

    public List<Sensor> findAll() {
        return sensorsRepository.findAll();
    }

    public Sensor findOne(int id) {
        return sensorsRepository.findById(id).orElseThrow(SensorNotFoundException::new);
    }

    public Optional<Sensor> findById(int id) {
        return sensorsRepository.findById(id);
    }

    public boolean hasSensor(String name) {
        return sensorsRepository.findByName(name).isPresent();
    }

    @Transactional
    public void save(Sensor sensor) {
        enrichSensor(sensor);
        sensorsRepository.save(sensor);
    }

    private void enrichSensor(Sensor sensor) {
        sensor.setCreatedAt(LocalDateTime.now());
        sensor.setUpdateAt(LocalDateTime.now());
    }
}
