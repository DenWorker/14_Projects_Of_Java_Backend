package com.example.SensorsRegistrationsRestAPI.utilMeasurements;

import com.example.SensorsRegistrationsRestAPI.models.Measurement;
import com.example.SensorsRegistrationsRestAPI.services.SensorsService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class MeasurementValidator implements Validator {
    private final SensorsService sensorsService;

    @Autowired
    public MeasurementValidator(SensorsService sensorsService) {
        this.sensorsService = sensorsService;
    }


    @Override
    public boolean supports(@NotNull Class<?> aClass) {
        return Measurement.class.equals(aClass);
    }

    @Override
    public void validate(@NotNull Object o, @NotNull Errors errors) {
        Measurement measurement = (Measurement) o;
        if (sensorsService.findById(measurement.getSensor().getId()).isEmpty()) {
            errors.rejectValue("sensor", "", "Not found sensor with this id!");
        }
    }
}
