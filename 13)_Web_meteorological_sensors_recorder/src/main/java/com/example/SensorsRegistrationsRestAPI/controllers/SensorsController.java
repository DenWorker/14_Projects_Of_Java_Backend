package com.example.SensorsRegistrationsRestAPI.controllers;

import com.example.SensorsRegistrationsRestAPI.dto.SensorDTO;
import com.example.SensorsRegistrationsRestAPI.models.Sensor;
import com.example.SensorsRegistrationsRestAPI.services.SensorsService;
import com.example.SensorsRegistrationsRestAPI.utilSensors.SensorErrorResponse;
import com.example.SensorsRegistrationsRestAPI.utilSensors.SensorNotCreatedException;
import com.example.SensorsRegistrationsRestAPI.utilSensors.SensorNotFoundException;
import com.example.SensorsRegistrationsRestAPI.utilSensors.SensorValidator;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sensors")
public class SensorsController {

    private final SensorsService sensorsService;
    private final SensorValidator sensorValidator;
    private final ModelMapper modelMapper;

    @Autowired
    public SensorsController(SensorsService sensorsService, SensorValidator sensorValidator, ModelMapper modelMapper) {
        this.sensorsService = sensorsService;
        this.sensorValidator = sensorValidator;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    public List<SensorDTO> getSensorsDTO() {
        return sensorsService.findAll().stream().map(this::convertToSensorDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/notDTO")
    public List<Sensor> getSensors() {
        return sensorsService.findAll();
    }

    @GetMapping("/{id}")
    public SensorDTO findSensorById(@PathVariable int id) {
        return convertToSensorDTO(sensorsService.findOne(id));
    }

    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> createSensor(@RequestBody @Valid SensorDTO sensorDTO,
                                                   BindingResult bindingResult) {
        sensorValidator.validate(convertToSensor(sensorDTO), bindingResult);
        if (bindingResult.hasErrors()) {
            StringBuilder errorsMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            errors.forEach(t -> errorsMsg.append(t.getField()).append("-").append(t.getDefaultMessage()).append(";"));
            throw new SensorNotCreatedException(errorsMsg.toString());
        }
        sensorsService.save(convertToSensor(sensorDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    //////////////////////////////////////////////////////////////////////////////////

    @ExceptionHandler
    private ResponseEntity<SensorErrorResponse> handleException(SensorNotFoundException exception) {
        SensorErrorResponse response = new SensorErrorResponse(
                "Sensor with this id wasn't found!",
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<SensorErrorResponse> handleException(SensorNotCreatedException exception) {
        SensorErrorResponse response = new SensorErrorResponse(
                exception.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private Sensor convertToSensor(SensorDTO sensorDTO) {
        return modelMapper.map(sensorDTO, Sensor.class);
    }

    private SensorDTO convertToSensorDTO(Sensor sensor) {
        return modelMapper.map(sensor, SensorDTO.class);
    }
}
