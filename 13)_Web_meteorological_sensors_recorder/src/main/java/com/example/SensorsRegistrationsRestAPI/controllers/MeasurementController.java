package com.example.SensorsRegistrationsRestAPI.controllers;

import com.example.SensorsRegistrationsRestAPI.dto.MeasurementDTO;
import com.example.SensorsRegistrationsRestAPI.models.Measurement;
import com.example.SensorsRegistrationsRestAPI.services.MeasurementsService;
import com.example.SensorsRegistrationsRestAPI.utilMeasurements.MeasurementErrorResponse;
import com.example.SensorsRegistrationsRestAPI.utilMeasurements.MeasurementNotCreatedException;
import com.example.SensorsRegistrationsRestAPI.utilMeasurements.MeasurementValidator;
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
@RequestMapping("/measurements")
public class MeasurementController {
    private final MeasurementsService measurementsService;
    private final MeasurementValidator measurementValidator;
    private final ModelMapper modelMapper;


    @Autowired
    public MeasurementController(MeasurementsService measurementsService, MeasurementValidator measurementValidator, ModelMapper modelMapper) {
        this.measurementsService = measurementsService;
        this.measurementValidator = measurementValidator;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    public List<MeasurementDTO> getMeasurements() {
        return measurementsService.findAll().stream().map(this::convertToMeasurementDTO).collect(Collectors.toList());
    }

    @GetMapping("/notDTO")
    public List<Measurement> getMeasurementsNotDTO() {
        return measurementsService.findAll();
    }

    @GetMapping("/{id}")
    public Measurement getMeasurementById(@PathVariable int id) {
        return measurementsService.findById(id);
    }

    @GetMapping("/rainyDaysCount")
    public long getRainyDaysCount() {
        return measurementsService.findAll().stream().filter(Measurement::getRaining).count();
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> addMeasurement(@RequestBody @Valid MeasurementDTO measurementDTO,
                                                     BindingResult bindingResult) {
        measurementValidator.validate(convertToMeasurement(measurementDTO), bindingResult);
        if (bindingResult.hasErrors()) {
            StringBuilder errorsMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            errors.forEach(t -> errorsMsg.append(t.getField()).append("-").append(t.getDefaultMessage()).append(";"));
            throw new MeasurementNotCreatedException(errorsMsg.toString());
        }
        measurementsService.save(convertToMeasurement(measurementDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    //////////////////////////////////////////////////////////////////////////////////
    @ExceptionHandler
    private ResponseEntity<MeasurementErrorResponse> handleException() {
        MeasurementErrorResponse response = new MeasurementErrorResponse(
                "Measurement with this id wasn't found!",
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<MeasurementErrorResponse> handleException(MeasurementNotCreatedException exception) {
        MeasurementErrorResponse response = new MeasurementErrorResponse(
                exception.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private MeasurementDTO convertToMeasurementDTO(Measurement measurement) {
        return modelMapper.map(measurement, MeasurementDTO.class);
    }

    private Measurement convertToMeasurement(MeasurementDTO measurementDTO) {
        return modelMapper.map(measurementDTO, Measurement.class);
    }
}
