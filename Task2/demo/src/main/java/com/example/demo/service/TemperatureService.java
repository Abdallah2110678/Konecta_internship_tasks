package com.example.demo.service;

import com.example.demo.enums.TemperatureUnit;
import com.example.demo.exception.InvalidUnitException;
import org.springframework.stereotype.Service;

@Service
public class TemperatureService {

    public double convert(String fromUnit, String toUnit, double value) {
        TemperatureUnit from = TemperatureUnit.fromString(fromUnit);
        TemperatureUnit to = TemperatureUnit.fromString(toUnit);

        // Convert to Celsius first, then to target unit
        double celsius = toCelsius(from, value);
        return fromCelsius(to, celsius);
    }

    public String getFormula(String fromUnit, String toUnit, double value, double result) {
        TemperatureUnit from = TemperatureUnit.fromString(fromUnit);
        TemperatureUnit to = TemperatureUnit.fromString(toUnit);

        if (from == TemperatureUnit.CELSIUS && to == TemperatureUnit.FAHRENHEIT) {
            return String.format("(%.1f°C × 9/5) + 32 = %.1f°F", value, result);
        } else if (from == TemperatureUnit.FAHRENHEIT && to == TemperatureUnit.CELSIUS) {
            return String.format("(%.1f°F - 32) × 5/9 = %.1f°C", value, result);
        } else if (from == TemperatureUnit.CELSIUS && to == TemperatureUnit.KELVIN) {
            return String.format("%.1f°C + 273.15 = %.1fK", value, result);
        } else if (from == TemperatureUnit.KELVIN && to == TemperatureUnit.CELSIUS) {
            return String.format("%.1fK - 273.15 = %.1f°C", value, result);
        } else if (from == TemperatureUnit.FAHRENHEIT && to == TemperatureUnit.KELVIN) {
            return String.format("(%.1f°F - 32) × 5/9 + 273.15 = %.1fK", value, result);
        } else if (from == TemperatureUnit.KELVIN && to == TemperatureUnit.FAHRENHEIT) {
            return String.format("(%.1fK - 273.15) × 9/5 + 32 = %.1f°F", value, result);
        }
        return String.format("%.1f %s = %.1f %s", value, fromUnit, result, toUnit);
    }

    private double toCelsius(TemperatureUnit from, double value) {
        switch (from) {
            case CELSIUS:
                return value;
            case FAHRENHEIT:
                return (value - 32) * 5.0 / 9.0;
            case KELVIN:
                return value - 273.15;
            default:
                throw new InvalidUnitException("Unsupported temperature unit: " + from);
        }
    }

    private double fromCelsius(TemperatureUnit to, double celsius) {
        switch (to) {
            case CELSIUS:
                return celsius;
            case FAHRENHEIT:
                return celsius * 9.0 / 5.0 + 32;
            case KELVIN:
                return celsius + 273.15;
            default:
                throw new InvalidUnitException("Unsupported temperature unit: " + to);
        }
    }
}