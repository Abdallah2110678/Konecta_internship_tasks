# Konecta Internship Tasks

This repository contains all my full-stack tasks completed during my internship at Konecta.

## Task 1: Shopping Cart Web Application

A responsive shopping cart built with **HTML**, **CSS**, and **JavaScript**.

### Features
- Display products with prices
- Add items to cart
- Update quantities and remove items
- Calculate total price
- Responsive design
- Local storage persistence

### Technologies
- HTML5, CSS3, JavaScript
- Local Storage API

---

## Task 2: Convertly Unit Converter REST API

A **Spring Boot REST API** that converts units across different categories.

### Features
- Convert temperature, length, weight, and time units
- Input validation with error handling
- Detailed conversion formulas in responses
- Swagger documentation

### API Endpoints
- `POST /convert` - Convert units
- `GET /categories` - Get all categories
- `GET /units?category={name}` - Get units for category
- `GET /sample-payload` - Get sample request
- `GET /health` - Health check

### Supported Units
- **Temperature**: Celsius, Fahrenheit, Kelvin
- **Length**: Meter, Kilometer, Mile, Inch, Foot
- **Weight**: Gram, Kilogram, Pound, Ounce
- **Time**: Seconds, Minutes, Hours, Days

### Technologies
- Java 17, Spring Boot 3.1.5
- Maven, Swagger/OpenAPI
- Jakarta Validation

---

## Setup Instructions

### Task 1
```bash
cd Task1
# Open index.html in browser
```

### Task 2
```bash
cd Task2
# API: http://localhost:8080
# Swagger: http://localhost:8080/swagger-ui.html
```
