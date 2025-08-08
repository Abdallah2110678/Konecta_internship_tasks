package com.example.demo.model;

public class ConversionResponse {
    private Double result;
    private String formula;
    private ConversionRequest originalInput;
    private String status;

    // Constructors
    public ConversionResponse() {}

    public ConversionResponse(Double result, String formula, ConversionRequest originalInput, String status) {
        this.result = result;
        this.formula = formula;
        this.originalInput = originalInput;
        this.status = status;
    }

    // Getters and Setters
    public Double getResult() {
        return result;
    }

    public void setResult(Double result) {
        this.result = result;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public ConversionRequest getOriginalInput() {
        return originalInput;
    }

    public void setOriginalInput(ConversionRequest originalInput) {
        this.originalInput = originalInput;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}