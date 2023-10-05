package com.example.SensorsRegistrationsRestAPI.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "measurements")
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "value")
    @NotNull(message = "Value should not be empty!")
    @DecimalMin(value = "-100.0")
    @DecimalMax(value = "100.0")
    private Double value;

    @Column(name = "raining")
    @NotNull(message = "Raining should not be empty!")
    private Boolean raining;

    @Column(name = "measurement_time")
    private LocalDateTime measurementTime;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne()
    @JoinColumn(name = "sensor_id", referencedColumnName = "id")
    @NotNull(message = "Sensor should not be empty!")
    private Sensor sensor;

    public Measurement() {
    }

    public Measurement(Integer id, @NotNull Double value, @NotNull Boolean raining, @NotNull Sensor sensor) {
        this.id = id;
        this.value = value;
        this.raining = raining;
        this.sensor = sensor;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public LocalDateTime getMeasurementTime() {
        return measurementTime;
    }

    public void setMeasurementTime(LocalDateTime measurementTime) {
        this.measurementTime = measurementTime;
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
        return "Measurement{" +
                "id=" + id +
                ", value=" + value +
                ", raining=" + raining +
                ", measurementTime=" + measurementTime +
                ", sensor=" + sensor +
                '}';
    }
}
