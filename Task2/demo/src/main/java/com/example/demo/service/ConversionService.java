package com.example.demo.service;

import com.example.demo.enums.Category;
import com.example.demo.exception.InvalidUnitException;
import com.example.demo.model.ConversionRequest;
import com.example.demo.model.ConversionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConversionService {

    @Autowired
    private TemperatureService temperatureService;

    @Autowired
    private LengthService lengthService;

    private static final Map<String, List<String>> CATEGORY_UNITS = new HashMap<>();

    static {
        CATEGORY_UNITS.put("temperature", Arrays.asList("celsius", "fahrenheit", "kelvin"));
        CATEGORY_UNITS.put("length", Arrays.asList("meter", "kilometer", "mile", "inch", "foot"));
        CATEGORY_UNITS.put("weight", Arrays.asList("gram", "kilogram", "pound", "ounce"));
        CATEGORY_UNITS.put("time", Arrays.asList("seconds", "minutes", "hours", "days"));
    }

    public ConversionResponse convert(ConversionRequest request) {
        validateRequest(request);

        Category category = Category.fromString(request.getCategory());
        double result = 0.0;
        String formula = "";

        switch (category) {
            case TEMPERATURE:
                result = temperatureService.convert(request.getFromUnit(), request.getToUnit(), request.getValue());
                formula = temperatureService.getFormula(request.getFromUnit(), request.getToUnit(), request.getValue(), result);
                break;
            case LENGTH:
                result = lengthService.convert(request.getFromUnit(), request.getToUnit(), request.getValue());
                formula = lengthService.getFormula(request.getFromUnit(), request.getToUnit(), request.getValue(), result);
                break;
            case WEIGHT:
                result = convertWeight(request.getFromUnit(), request.getToUnit(), request.getValue());
                formula = String.format("%.3f %s = %.3f %s", request.getValue(), request.getFromUnit(), result, request.getToUnit());
                break;
            case TIME:
                result = convertTime(request.getFromUnit(), request.getToUnit(), request.getValue());
                formula = String.format("%.3f %s = %.3f %s", request.getValue(), request.getFromUnit(), result, request.getToUnit());
                break;
        }

        return new ConversionResponse(result, formula, request, "success");
    }

    public List<String> getCategories() {
        return Arrays.asList("temperature", "length", "weight", "time");
    }

    public List<String> getUnitsForCategory(String category) {
        if (!CATEGORY_UNITS.containsKey(category.toLowerCase())) {
            throw new InvalidUnitException("Invalid category: " + category);
        }
        return CATEGORY_UNITS.get(category.toLowerCase());
    }

    public ConversionRequest getSamplePayload() {
        return new ConversionRequest("temperature", "celsius", "fahrenheit", 25.0);
    }

    private void validateRequest(ConversionRequest request) {
        // Validate category
        try {
            Category.fromString(request.getCategory());
        } catch (IllegalArgumentException e) {
            throw new InvalidUnitException("Invalid category: " + request.getCategory());
        }

        // Validate units belong to category
        List<String> validUnits = getUnitsForCategory(request.getCategory());
        if (!validUnits.contains(request.getFromUnit().toLowerCase())) {
            throw new InvalidUnitException("Unit '" + request.getFromUnit() + "' does not belong to category '" + request.getCategory() + "'");
        }
        if (!validUnits.contains(request.getToUnit().toLowerCase())) {
            throw new InvalidUnitException("Unit '" + request.getToUnit() + "' does not belong to category '" + request.getCategory() + "'");
        }

        // Validate non-negative values for weight and time
        if ((request.getCategory().equalsIgnoreCase("weight") || request.getCategory().equalsIgnoreCase("time")) 
            && request.getValue() < 0) {
            throw new InvalidUnitException("Value cannot be negative for " + request.getCategory());
        }
    }

    private double convertWeight(String fromUnit, String toUnit, double value) {
        Map<String, Double> toGrams = new HashMap<>();
        toGrams.put("gram", 1.0);
        toGrams.put("kilogram", 1000.0);
        toGrams.put("pound", 453.592);
        toGrams.put("ounce", 28.3495);

        if (!toGrams.containsKey(fromUnit.toLowerCase()) || !toGrams.containsKey(toUnit.toLowerCase())) {
            throw new InvalidUnitException("Invalid weight unit");
        }

        double grams = value * toGrams.get(fromUnit.toLowerCase());
        return grams / toGrams.get(toUnit.toLowerCase());
    }

    private double convertTime(String fromUnit, String toUnit, double value) {
        Map<String, Double> toSeconds = new HashMap<>();
        toSeconds.put("seconds", 1.0);
        toSeconds.put("minutes", 60.0);
        toSeconds.put("hours", 3600.0);
        toSeconds.put("days", 86400.0);

        if (!toSeconds.containsKey(fromUnit.toLowerCase()) || !toSeconds.containsKey(toUnit.toLowerCase())) {
            throw new InvalidUnitException("Invalid time unit");
        }

        double seconds = value * toSeconds.get(fromUnit.toLowerCase());
        return seconds / toSeconds.get(toUnit.toLowerCase());
    }
}