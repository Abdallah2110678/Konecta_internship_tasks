package com.example.demo.controllers;

import com.example.demo.model.ConversionRequest;
import com.example.demo.model.ConversionResponse;
import com.example.demo.service.ConversionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
public class ConverterController {

    @Autowired
    private ConversionService conversionService;

    @PostMapping("/convert")
    public ResponseEntity<ConversionResponse> convert(@Valid @RequestBody ConversionRequest request) {
        ConversionResponse response = conversionService.convert(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories() {
        List<String> categories = conversionService.getCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/units")
    public ResponseEntity<List<String>> getUnits(@RequestParam String category) {
        List<String> units = conversionService.getUnitsForCategory(category);
        return ResponseEntity.ok(units);
    }

    @GetMapping("/sample-payload")
    public ResponseEntity<ConversionRequest> getSamplePayload() {
        ConversionRequest sample = conversionService.getSamplePayload();
        return ResponseEntity.ok(sample);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "Unit Converter API is up and running");
        return ResponseEntity.ok(status);
    }
}