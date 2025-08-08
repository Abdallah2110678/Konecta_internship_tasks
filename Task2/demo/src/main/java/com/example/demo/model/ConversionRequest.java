package com.example.demo.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ConversionRequest {
    @NotBlank(message = "Category is required")
    private String category;
    
    @NotBlank(message = "From unit is required")
    private String fromUnit;
    
    @NotBlank(message = "To unit is required")
    private String toUnit;
    
    @NotNull(message = "Value is required")
    private Double value;

    // Constructors
    public ConversionRequest() {}

    public ConversionRequest(String category, String fromUnit, String toUnit, Double value) {
        this.category = category;
        this.fromUnit = fromUnit;
        this.toUnit = toUnit;
        this.value = value;
    }

    // Getters and Setters
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFromUnit() {
        return fromUnit;
    }

    public void setFromUnit(String fromUnit) {
        this.fromUnit = fromUnit;
    }

    public String getToUnit() {
        return toUnit;
    }

    public void setToUnit(String toUnit) {
        this.toUnit = toUnit;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}