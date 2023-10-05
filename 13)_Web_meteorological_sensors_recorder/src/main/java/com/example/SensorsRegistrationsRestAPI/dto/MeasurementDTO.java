package com.example.SensorsRegistrationsRestAPI.dto;

import com.example.SensorsRegistrationsRestAPI.models.Sensor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;


public class MeasurementDTO {

    @NotNull(message = "Value should not be empty!")
    @DecimalMin(value = "-100.0")
    @DecimalMax(value = "100.0")
    private Double value = 0.0;

    @NotNull(message = "Raining should not be empty!")
    private Boolean raining = false;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @NotNull(message = "Sensor should not be empty!")
    private Sensor sensor = new Sensor();

    public MeasurementDTO(@NotNull Double value, @NotNull Boolean raining, @NotNull Sensor sensor) {
        this.value = value;
        this.raining = raining;
        this.sensor = sensor;
    }

    public MeasurementDTO() {
    }

    @NotNull
    public Double getValue() {
        return value;
    }

    public void setValue(@NotNull Double value) {
        this.value = value;
    }

    @NotNull
    public Boolean getRaining() {
        return raining;
    }

    public void setRaining(@NotNull Boolean raining) {
        this.raining = raining;
    }

    @NotNull
    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(@NotNull Sensor sensor) {
        this.sensor = sensor;
    }

    @Override
    public String toString() {
        return "\n" +
                "sensor=" + sensor +
                ", value=" + value +
                ", raining=" + raining +
                '}' + "\n";
    }
}
