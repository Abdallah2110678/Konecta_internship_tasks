package com.example.demo.enums;

public enum TemperatureUnit {
    CELSIUS("celsius"),
    FAHRENHEIT("fahrenheit"),
    KELVIN("kelvin");

    private final String value;

    TemperatureUnit(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    // Add this missing method
    public static TemperatureUnit fromString(String value) {
        for (TemperatureUnit unit : TemperatureUnit.values()) {
            if (unit.value.equalsIgnoreCase(value)) {
                return unit;
            }
        }
        throw new IllegalArgumentException("Invalid temperature unit: " + value);
    }
}