package com.example.demo.service;

import com.example.demo.exception.InvalidUnitException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LengthService {
    
    private static final Map<String, Double> TO_METERS = new HashMap<>();
    
    static {
        TO_METERS.put("meter", 1.0);
        TO_METERS.put("kilometer", 1000.0);
        TO_METERS.put("mile", 1609.344);
        TO_METERS.put("inch", 0.0254);
        TO_METERS.put("foot", 0.3048);
    }

    public double convert(String fromUnit, String toUnit, double value) {
        if (!TO_METERS.containsKey(fromUnit.toLowerCase())) {
            throw new InvalidUnitException("Invalid length unit: " + fromUnit);
        }
        if (!TO_METERS.containsKey(toUnit.toLowerCase())) {
            throw new InvalidUnitException("Invalid length unit: " + toUnit);
        }

        double meters = value * TO_METERS.get(fromUnit.toLowerCase());
        return meters / TO_METERS.get(toUnit.toLowerCase());
    }

    public String getFormula(String fromUnit, String toUnit, double value, double result) {
        return String.format("%.3f %s = %.3f %s", value, fromUnit, result, toUnit);
    }
}